package jp.co.gas.tokyo.accessLogBatch.mapper;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import jp.co.gas.tokyo.accessLogBatch.utilities.DataSourceUtil;

@Configuration
@MapperScan(basePackages = PrimaryDbConfig.BASE_PACKAGES, sqlSessionTemplateRef = "primarySqlSessionTemplate")
public class PrimaryDbConfig {

	public static final String BASE_PACKAGES = "jp.co.gas.tokyo.accessLogBatch.mapper";
	public static final String MAPPER_XML_PATH = "jp/co/gas/tokyo/accessLogBatch/mapper/*/*.xml";

	@Bean(name = "primaryDataSourceUtil")
	@ConfigurationProperties(prefix = "spring.datasource")
	DataSourceUtil genDataSourceUtil() {
		return new DataSourceUtil();
	}

	@Primary
	@Bean(name = "primaryDataSource")
	public DataSource dataSource() {
		DataSourceUtil dsu = genDataSourceUtil();
		return dsu.getDataSource();
	}

	@Primary
	@Bean(name = "primarySqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("primaryDataSource") DataSource primaryDataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(primaryDataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_XML_PATH));
		return bean.getObject();
	}

	@Bean(name = "primarySqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(
			@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean(name = "primaryTransactionManager")
	public DataSourceTransactionManager dataSourceTransactionManager(
			@Qualifier("primaryDataSource") DataSource datasource) {
		DataSourceTransactionManager txm = new DataSourceTransactionManager(datasource);
		txm.setRollbackOnCommitFailure(true);
		return txm;
	}
}