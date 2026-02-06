#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}


# copy sql
echo "begin copy sql "
cp ../sql/ry_20250523.sql ./mysql/db
cp ../sql/ry_config_20250902.sql ./mysql/db

# copy html
echo "begin copy html "
cp -r ../qa-hero-ui/dist/** ./nginx/html/dist


# copy jar
echo "begin copy qa-hero-gateway "
cp ../qa-hero-gateway/target/qa-hero-gateway.jar ./ruoyi/gateway/jar

echo "begin copy qa-hero-auth "
cp ../qa-hero-auth/target/qa-hero-auth.jar ./ruoyi/auth/jar

echo "begin copy qa-hero-visual "
cp ../qa-hero-visual/qa-hero-monitor/target/qa-hero-visual-monitor.jar  ./ruoyi/visual/monitor/jar

echo "begin copy qa-hero-modules-system "
cp ../qa-hero-modules/qa-hero-system/target/qa-hero-modules-system.jar ./ruoyi/modules/system/jar

echo "begin copy qa-hero-modules-file "
cp ../qa-hero-modules/qa-hero-file/target/qa-hero-modules-file.jar ./ruoyi/modules/file/jar

echo "begin copy qa-hero-modules-job "
cp ../qa-hero-modules/qa-hero-job/target/qa-hero-modules-job.jar ./ruoyi/modules/job/jar

echo "begin copy qa-hero-modules-gen "
cp ../qa-hero-modules/qa-hero-gen/target/qa-hero-modules-gen.jar ./ruoyi/modules/gen/jar

