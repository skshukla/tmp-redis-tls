
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"


docker kill ssl_redis_container || true;
docker rm ssl_redis_container || true;
rm -rf $SCRIPT_DIR/certs/*

cd $SCRIPT_DIR
docker build -t ssl_redis_image -f Dockerfile .
cd -
docker run -d \
  -p 6379:6379  \
  --name ssl_redis_container ssl_redis_image


echo 'docker exec -it ssl_redis_container sh'
echo 'redis-cli --tls --cert tests/tls/ca.crt --key tests/tls/redis.key --cacert tests/tls/ca.crt'