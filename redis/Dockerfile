# https://www.appsloveworld.com/docker/100/19/how-to-set-up-a-docker-redis-container-with-ssl
# https://redis.io/docs/management/security/encryption/
# https://spin.atomicobject.com/2021/08/05/configuring-redis-tls/
# Plain redis-cli command will not work
# redis-cli --tls --cert tests/tls/ca.crt --key tests/tls/redis.key --cacert tests/tls/ca.crt

FROM redis:7 as base
RUN apt-get update && apt-get install openssl
USER redis
COPY --chown=redis:redis ./generate_certificates.sh ./
COPY --chown=redis:redis ./redis.conf /data
RUN chmod +x ./generate_certificates.sh
RUN ./generate_certificates.sh
#CMD ["redis-server", "--tls-port", "6379", "--port", "0", "--tls-cert-file", "tests/tls/ca.crt", "--tls-key-file", "tests/tls/redis.key", "--tls-ca-cert-file", "tests/tls/ca.crt"]
CMD ["redis-server", "/data/redis.conf"]

