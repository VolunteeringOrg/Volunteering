#!/bin/sh
DB_TCP_ADDR=172.18.0.5
DB_TCP_PORT=1521

set -e
while ! curl http://$DB_TCP_ADDR:$DB_TCP_PORT/
do
  echo "$(date) - still trying"
  sleep 10
done
echo "$(date) - connected successfully"
