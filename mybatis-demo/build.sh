env=prod
if [ -n "$1" ]; then
  env=$1
fi

# -Dmaven.test.skip=true  不执行测试用例，也不编译测试用例类至target/test-classes
mvn clean package -P$env -DskipTests
