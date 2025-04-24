package jp.co.gas.tokyo.accessLogBatch.batch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class QuotedBeanWrapperFieldExtractor<T> implements FieldExtractor<T>, InitializingBean {

	private String quote = "";

	private String[] names;

	public void setQuote(char quote) {
		this.quote = String.valueOf(quote);
	}

	/**
	 * @param names field names to be extracted by the {@link #extract(Object)}
	 * method.
	 */
	public void setNames(String[] names) {
		Assert.notNull(names, "Names must be non-null");
		this.names = Arrays.asList(names).toArray(new String[names.length]);
	}

	/**
	 * @see org.springframework.batch.item.file.transform.FieldExtractor#extract(java.lang.Object)
	 */
	@Override
	public Object[] extract(T item) {
		List<Object> values = new ArrayList<Object>();

		BeanWrapper bw = new BeanWrapperImpl(item);
		for (String propertyName : this.names) {
			Object object = bw.getPropertyValue(propertyName);
			// 文字列に変換
			String s = String.valueOf(object);
			// 文字列にクォートが含まれていたらエスケープ
			if (quote.length() > 0) {
				s = s.replaceAll(quote, quote + quote);
			}
			// 前後にクォートを付加
			values.add(quote + s + quote);
		}
		return values.toArray();
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(names, "The 'names' property must be set.");
	}
}
