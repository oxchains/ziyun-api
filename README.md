### 紫云冷链demo monitoring
环境安装部署:

influxdb
    - 安装influxdb
        OS X (via Homebrew)
        brew update
        brew install influxdb

        Ubuntu & Debian (64-bit)
        wget https://dl.influxdata.com/influxdb/releases/influxdb_0.13.0_amd64.deb
        sudo dpkg -i influxdb_0.13.0_amd64.deb

    - 服务启动
        influxd
    - 进入命令行
        influx
    - 创建数据库
        命令行:create database mydb
        rest:curl -POST http://localhost:8086/query --data-urlencode "q=CREATE DATABASE mydb"
    - 教程
        http://www.linuxdaxue.com/how-to-install-influxdb.html



grafana
    - 文档
        http://docs.grafana.org/
    - 启动
        grafana-server --config=/usr/local/etc/grafana/grafana.ini --homepath /usr/local/share/grafana cfg:default.paths.logs=/usr/local/var/log/grafana cfg:default.paths.data=/usr/local/var/lib/grafana cfg:default.paths.plugins=/usr/local/var/lib/grafana/plugins
    - import
        monitor-board.json