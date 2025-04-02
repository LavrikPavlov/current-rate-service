echo "Create kafka topic for current service"

until kafka-topics --bootstrap-server kafka:9092 --list; do
  echo "Waiting for Kafka to be ready..."
  sleep 5
done

kafka-topics --create \
  --if-not-exists \
  --topic current.rate.info \
  --partitions 5 \
  --replication-factor 1 \
  --bootstrap-server kafka:9092

echo "Topic current.rate.info created successfully"