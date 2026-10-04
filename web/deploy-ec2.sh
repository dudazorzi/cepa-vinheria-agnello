#!/usr/bin/env bash
# Execute manualmente na instância Ubuntu EC2, dentro da pasta web/ do repositório.
set -euo pipefail

if [[ "$(id -u)" -eq 0 ]]; then
  echo "Execute como usuário ubuntu (com sudo), não como root."
  exit 1
fi
if [[ ! -f pom.xml || ! -f mvnw ]]; then
  echo "Entre na pasta web/ antes de executar: bash ./deploy-ec2.sh"
  exit 1
fi
if [[ ! -f /etc/os-release ]] || ! grep -q '^ID=ubuntu$' /etc/os-release; then
  echo "Este roteiro foi preparado para Ubuntu Server, como no relatório AWS."
  exit 1
fi

echo "Instalando Java 17, Tomcat 10, curl e OpenSSL na EC2..."
sudo apt-get update
sudo apt-get install -y openjdk-17-jdk-headless tomcat10 curl openssl

architecture="$(dpkg --print-architecture)"
export JAVA_HOME="/usr/lib/jvm/java-17-openjdk-${architecture}"
if [[ ! -x "$JAVA_HOME/bin/java" ]]; then
  echo "Java 17 não encontrado em $JAVA_HOME; ajuste JAVA_HOME manualmente."
  exit 1
fi

echo "Compilando e testando o projeto web..."
sh ./mvnw -B package

echo "Configurando a persistência no volume EBS e a senha do painel..."
sudo install -d -m 0700 /etc/cepa
if ! sudo test -f /etc/cepa/cepa.env; then
  password="$(openssl rand -hex 18)"
  printf 'CEPA_ADMIN_PASSWORD=%s\n' "$password" | sudo tee /etc/cepa/cepa.env > /dev/null
  unset password
fi
sudo chmod 0600 /etc/cepa/cepa.env
sudo install -d -m 0755 /etc/systemd/system/tomcat10.service.d
dropin="$(mktemp)"
trap 'rm -f "$dropin"' EXIT
printf '[Service]\nEnvironment=JAVA_HOME=%s\nEnvironment=CEPA_DATA_FILE=/var/lib/cepa/vinhos.xml\nEnvironmentFile=/etc/cepa/cepa.env\nStateDirectory=cepa\nReadWritePaths=/var/lib/cepa\n' "$JAVA_HOME" > "$dropin"
sudo install -m 0644 "$dropin" /etc/systemd/system/tomcat10.service.d/cepa.conf
sudo systemctl daemon-reload
sudo install -d -m 0755 /var/lib/tomcat10/webapps
sudo install -m 0644 target/cepa.war /var/lib/tomcat10/webapps/cepa.war
sudo systemctl enable --now tomcat10
sudo systemctl restart tomcat10

echo "Aguardando resposta local da aplicação..."
for attempt in {1..30}; do
  if curl --fail --silent --output /dev/null http://127.0.0.1:8080/cepa/inicio \
      && sudo test -s /var/lib/cepa/vinhos.xml; then
    echo "CEPA respondeu em http://127.0.0.1:8080/cepa/inicio"
    echo "O catálogo persistente ficará em /var/lib/cepa/vinhos.xml."
    echo "Para consultar a senha do painel: sudo grep CEPA_ADMIN_PASSWORD /etc/cepa/cepa.env"
    echo "Não publique essa senha nem abra a porta 8080 para toda a internet sem HTTPS."
    exit 0
  fi
  sleep 2
done

echo "A aplicação não respondeu ou o catálogo persistente não foi criado."
echo "Consulte: sudo journalctl -u tomcat10 -n 80 --no-pager"
exit 1
