default: build

build:
	javac -classpath ./libs/dot-parser.jar:./libs/json-parser.jar ./src/*.java -d ./target/

run: build
	java -classpath ./libs/dot-parser.jar:./libs/json-parser.jar:./target StagServer data/entities.dot data/actions.json

run-client: build
	java -classpath ./target StagClient Philipp

clean:
	rm ./target/*.class