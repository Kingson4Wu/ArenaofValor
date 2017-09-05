### 常用的分库分表中间件

简单易用的组件：

当当sharding-jdbc: sharding­jdbc (https://github.com/dangdangdotcom/sharding-jdbc) 
蘑菇街TSharding: TSharding (https://github.com/baihui212/tsharding)
强悍重量级的中间件：

sharding : sharding (https://github.com/go­pg/sharding)
TDDL Smart Client的方式（淘宝）: TDDL Smart Client (https://github.com/alibaba/tb_tddl) 
Atlas(Qihoo 360): Atlas(Qihoo 360) (https://github.com/Qihoo360/Atlas)
alibaba.cobar(是阿里巴巴（B2B）部门开发): alibaba.cobar( B2B ) (https://github.com/alibaba/cobar)
MyCAT（基于阿里开源的Cobar产品而研发）:  MyCAT Cobar (http://www.mycat.org.cn/)
Oceanus(58同城数据库中间件)
OneProxy(支付宝首席架构师楼方鑫开发)
vitess（谷歌开发的数据库中间件）

目前市面上的分库分表中间件相对较多，其中基于代理方式的有MySQL Proxy和Amoeba，
基于Hibernate框架的是Hibernate Shards，基于jdbc的有当当sharding-jdbc，
基于mybatis的类似maven插件式的有蘑菇街的蘑菇街TSharding，通过重写spring的ibatis template类是Cobar Client，
这些框架各有各的优势与短板，架构师可以在深入调研之后结合项目的实际情况进行选择，但是总的来说，我个人对于框架的选择是持谨慎态度的。
一方面多数框架缺乏成功案例的验证，其成熟性与稳定性值得怀疑。
另一方面，一些从成功商业产品开源出框架（如阿里和淘宝的一些开源项目）是否适合你的项目是需要架构师深入调研分析的。
当然，最终的选择一定是基于项目特点、团队状况、技术门槛和学习成本等综合因素考量确定的。

作者：jackcooper
链接：http://www.jianshu.com/p/32b3e91aa22c
來源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。


### 事务
事务补偿（幂等值）

对于那些对性能要求很高，但对一致性要求并不高的系统，往往并不苛求系统的实时一致性，只要在一个允许的时间周期内达到最终一致性即可，这使得事务补偿机制成为一种可行的方案。
事务补偿的实现与系统业务紧密相关，并没有一种标准的处理方式。一些常见的实现方式有：对数据进行对帐检查;基于日志进行比对;定期同标准数据来源进行同步，等等。


### Twitter的分布式自增ID算法Snowflake

在分布式系统中，需要生成全局UID的场合还是比较多的，twitter的snowflake解决了这种需求，实现也还是很简单的，除去配置信息，核心代码就是毫秒级时间41位 机器ID 10位 毫秒内序列12位。

10---0000000000 0000000000 0000000000 0000000000 0 --- 00000 ---00000 ---000000000000
在上面的字符串中，第一位为未使用（实际上也可作为long的符号位），接下来的41位为毫秒级时间，然后5位datacenter标识位，5位机器ID（并不算标识符，实际是为线程标识），然后12位该毫秒内的当前毫秒内的计数，加起来刚好64位，为一个Long型。

这样的好处是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞（由datacenter和机器ID作区分），并且效率较高，经测试，snowflake每秒能够产生26万ID左右，完全满足需要。

### 跨分片的排序分页
那如何解决分库情况下的分页问题呢？有以下几种办法：

如果是在前台应用提供分页，则限定用户只能看前面n页，这个限制在业务上也是合理的，一般看后面的分页意义不大（如果一定要看，可以要求用户缩小范围重新查询）。

如果是后台批处理任务要求分批获取数据，则可以加大page size，比如每次获取5000条记录，有效减少分页数（当然离线访问一般走备库，避免冲击主库）。

分库设计时，一般还有配套大数据平台汇总所有分库的记录，有些分页查询可以考虑走大数据平台。
