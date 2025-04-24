package jp.co.gas.tokyo.accessLogBatch.batch;

import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class BatchFactory {

	public static <T> FlatFileItemReader<T> flatFileItemReader(Resource resource, String[] names, String encoding, String delimiter, Character quote,
			Class<T> clazz) throws Exception {

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(delimiter);
		if (quote != null) {
			lineTokenizer.setQuoteCharacter(quote);
		}
		lineTokenizer.setNames(names);

		BeanWrapperFieldSetMapper<T> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(clazz);

		DefaultLineMapper<T> lineMapper = new DefaultLineMapper<>();
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);

		FlatFileItemReader<T> reader = new FlatFileItemReader<>();
		reader.setResource(resource);
		reader.setLineMapper(lineMapper);
		reader.setEncoding(encoding);

		reader.afterPropertiesSet();
		return reader;
	}

	public static <T> FlatFileItemWriter<T> flatFileItemWriter(Resource resource, String[] names, String encoding, String delimiter, Character quote,
			String lineSeparator, Class<T> clazz) throws Exception {

		QuotedBeanWrapperFieldExtractor<T> fieldExtractor = new QuotedBeanWrapperFieldExtractor<>();
		if (quote != null) {
			fieldExtractor.setQuote(quote);
		}
		fieldExtractor.setNames(names);

		DelimitedLineAggregator<T> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setFieldExtractor(fieldExtractor);
		lineAggregator.setDelimiter(delimiter);

		FlatFileItemWriter<T> writer = new FlatFileItemWriter<>();
		writer.setLineAggregator(lineAggregator);
		writer.setResource(resource);
		writer.setEncoding(encoding);
		writer.setLineSeparator(lineSeparator);

		writer.afterPropertiesSet();
		return writer;
	}

	public static <T> JdbcCursorItemReader<T> jdbcCursorItemReader(String sql, DataSource dataSource, Class<T> clazz) throws Exception {
		JdbcCursorItemReader<T> reader = new JdbcCursorItemReader<>();
		reader.setSql(sql);
		reader.setRowMapper(new BeanPropertyRowMapper<T>(clazz));
		reader.setDataSource(dataSource);

		reader.afterPropertiesSet();
		return reader;
	}

	public static <T> JdbcBatchItemWriter<T> jdbcBatchItemWriter(String sql, DataSource dataSource) {
		JdbcBatchItemWriter<T> writer = new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<T>());
		writer.setSql(sql);
		writer.setDataSource(dataSource);

		writer.afterPropertiesSet();
		return writer;
	}

}
