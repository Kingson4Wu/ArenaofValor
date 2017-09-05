package com.kxw.shardingjdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;

/**
 * Created by kingsonwu on 17/9/5.
 */
public class ShardingJdbcExample {

    public static void main(String[] args) throws SQLException {

        /*ShardingRule shardingRule = ShardingRule.builder()
            .dataSourceRule(dataSourceRule)
            .tableRules(tableRuleList)
            .databaseShardingStrategy(new DatabaseShardingStrategy("sharding_column", new XXXShardingAlgorithm()))
            .tableShardingStrategy(new TableShardingStrategy("sharding_column", new XXXShardingAlgorithm())))
        .build();*/
        ShardingRule shardingRule = ShardingRule.builder()

            .build();

        DataSource dataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
        String sql
            = "SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id WHERE o.user_id=? AND o"
            + ".order_id=?";
        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, 10);
            preparedStatement.setInt(2, 1001);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getInt(1));
                    System.out.println(rs.getInt(2));
                }
            }
        }
    }
}
