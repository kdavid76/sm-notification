#bin/bash

/usr/bin/kafka-topics --bootstrap-server localhost:9092 --create --topic sm-notifications --partitions 1 --replication-factor 1
/usr/bin/kafka-topics --bootstrap-server localhost:9092 --create --topic sm-audit-log --partitions 1 --replication-factor 1