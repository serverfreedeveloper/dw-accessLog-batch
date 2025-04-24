package jp.co.gas.tokyo.accessLogBatch.utilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriUtils;

/**
 * FileUtilクラス
 */
public class FileUtil {

	/**
	 * アクセスログ出力
	 * @param csv csv出力情報
	 * @param pathName パス
	 * @param charsetName キャラセット
	 * @throws IOException IOException
	 */
	public static void printCsv(String csv, String pathName, String charsetName) throws IOException {
		Path outFilePath = Paths.get(pathName);
		Charset outCharset = Charset.forName(charsetName);

		Writer writer = null;
		try {
			writer = Files.newBufferedWriter(outFilePath, outCharset);
			writer.write(csv);
		} catch (Exception e) {
			throw new IOException("アクセスログの出力に失敗しました");
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
					throw new IOException("アクセスログの出力に失敗しました");
				}
			}
		}
	}

	/**
	 * CSV出力用 ヘッダー作成
	 *
	 * @param fileName ファイル名称
	 * @return HttpHeaders
	 */
	public static HttpHeaders getHeadersForCsv(String fileName) {

		HttpHeaders h = new HttpHeaders();
		String headerValue = String.format("attachment; filename=\"%s\"; filename*=UTF-8''%s",
				UriUtils.encode(fileName, StandardCharsets.UTF_8.name()),
				UriUtils.encode(fileName + ".csv", StandardCharsets.UTF_8.name()));
		h.add(HttpHeaders.CONTENT_DISPOSITION, headerValue);
		h.add(HttpHeaders.CONTENT_TYPE, "text/csv;");
		h.add(HttpHeaders.SET_COOKIE, "processCompleted=true; path=/");

		return h;

	}

	/**
	 * ZIP出力用 ヘッダー作成
	 *
	 * @param fileName ファイル名称
	 * @return HttpHeaders
	 * @throws UnsupportedEncodingException
	 */
	public static HttpHeaders getHeadersForZip(String fileName) throws UnsupportedEncodingException {

		HttpHeaders h = new HttpHeaders();
		String headerValue = String.format("attachment; filename=\"%s\"; filename*=UTF-8''%s", fileName,
				UriUtils.encode(fileName + ".zip", StandardCharsets.UTF_8.name()));
		h.add(HttpHeaders.CONTENT_DISPOSITION, headerValue);
		h.add(HttpHeaders.CONTENT_TYPE, "application/zip;");
		h.add(HttpHeaders.CONTENT_ENCODING, "binary");

		return h;

	}

	/**
	 * CSV出力用 BOM付きに変換
	 *
	 * @param CsvBody CSV出力項目
	 * @return ByteBuffer
	 */
	public static ByteBuffer makeBomForCsv(String CsvBody) {

		byte[] bom = new byte[] { (byte) 0xef, (byte) 0xbb, (byte) 0xbf };
		byte[] body = CsvBody.getBytes(StandardCharsets.UTF_8);

		byte[] data = new byte[bom.length + body.length];
		ByteBuffer buff = ByteBuffer.wrap(data);
		buff.put(bom);
		buff.put(body);

		return buff;

	}
}
