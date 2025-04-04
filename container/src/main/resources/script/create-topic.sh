echo "Create kafka topic for current service"

until kafka-topics --bootstrap-server kafka:29092 --list; do
  echo "Waiting for Kafka to be ready..."
  sleep 5
done

kafka-topics --create \
  --if-not-exists \
  --topic current.rate.info.rq \
  --partitions 5 \
  --replication-factor 1 \
  --bootstrap-server kafka:29092

  kafka-topics --create \
    --if-not-exists \
    --topic current.rate.info.rs \
    --partitions 5 \
    --replication-factor 1 \
    --bootstrap-server kafka:29092

echo "Topic current.rate.info created successfully"


kafka-topics --create \
    --if-not-exists \
    --topic current.rate.user.rq \
    --partitions 5 \
    --replication-factor 1 \
    --bootstrap-server kafka:29092

kafka-topics --create \
    --if-not-exists \
    --topic current.rate.user.rs \
    --partitions 5 \
    --replication-factor 1 \
    --bootstrap-server kafka:29092


echo "Topic current.rate.user created successfully"