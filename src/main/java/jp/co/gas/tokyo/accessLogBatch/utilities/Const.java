package jp.co.gas.tokyo.accessLogBatch.utilities;

/**
 * 定数、メッセージ管理
 */
public class Const {

	/** ログ出力メッセージ **/
	public static final String START_MESSAGE = "処理を開始します。";
	public static final String NORMAL_END_MESSAGE = "処理を終了します。";
	public static final String WARNING_END_MESSAGE = "処理を終了します。（警告終了）";
	public static final String ABNORMAL_END_MASSAGE = "例外が発生しました。バッチ処理を終了します。";
	public static final String STRING_EXCEPTION = "Exception";

	/** 定数 */
	public static final String TG_CRM_RENKEI_SIGN_SYUTURYOKUZUMI = "1";

	// バッチジョブの終了コード
	public static final int NORMAL_CD = 0; // 正常終了
	public static final int WARNING_CD = 1; // 警告終了
	public static final int ERROR_CD = 9; // 異常終了

	/** コード */
	public static final String BATCH_NAME_TIHBT0001 = "BT0001";
	public static final String DELIMITER = "\t";
	public static final String LINE_SEPARATOR = "\n";

	// 商材
	public static final String SYOZAI_IPPAN = "P00101";
	public static final String SYOZAI_ZUTTOMO_GAS = "P00102";
	public static final String SYOZAI_KATEI = "P00103";
	public static final String SYOZAI_DENKI = "P00201";
	public static final String SYOZAI_TIH = "P00810";

	// 申込種別
	public static final String MOSIKOMI_SYB_HK = "T00300";

	//受付チャネル
	public static final String UKETUKE_CHANNEL_PHONE = "C00050";
	public static final String UKETUKE_CHANNEL_WEB = "C00010";
	public static final String UKETUKE_CHANNEL_PAPER = "C00020";
	public static final String UKETUKE_CHANNEL_YUBIN = "C00030";
	public static final String UKETUKE_CHANNEL_MNTI = "C00040";
	public static final String UKETUKE_CHANNEL_WEBPAPER = "C00045";

	//受付システム
	public static final String UKETUKE_SYSTEM_RELIA = "S00130";
	public static final String UKETUKE_SYSTEM_INTRA = "S00030";
	public static final String UKETUKE_SYSTEM_INTERNET = "S00031";
	public static final String UKETUKE_SYSTEM_MOBILE = "S00032";
	public static final String UKETUKE_SYSTEM_TGCARS = "S00150";

	// 申出人種別
	public static final String MSDNN_SYB_HN = "10";
	public static final String MSDNN_SYB_HGSYA = "20";
	public static final String MSDNN_SYB_HTDRN = "30";
	public static final String MSDNN_SYB_HN_HGSYA_HTDRN="40";
	public static final String MSDNN_SYB_DKYOSHINZK = "50";

	// 申出人属性
	public static final String MSDNN_SYB_ZKS_SNT = "90";

	// 法人/個人区分
	public static final String HJKJ_KBN_HMI = "9";

	// 担当部門
	public static final String TNT_BMN_LIVINGSERVICE= "0";

	// 生年月日不備事由
	public static final String BIRTHDAY_FBJY_CD_BIRTHDAY_SYTK_TISYOGI = "100";
	public static final String BIRTHDAY_FBJY_CD_MKNYU = "200";
	public static final String BIRTHDAY_FBJY_CD_SNT = "900";

	/**
	 * 接客履歴登録の返り値判定値
	 */
	public class RegisterCustomerHistoryResultCode {

		/**
		 * 201≒正常登録時
		 */
		public static final String STATUS_201 = "STATUS_201";

		/**
		 * 上記以外≒登録不備発生時
		 */
		public static final String OTHER = "OTHER";

	}

	/**
	 * 接客履歴登録の返り値判定値
	 */
	public class RegisterTCCAPI021ResultCode {

		/**
		 * 0:正常登録時
		 */
		public static final String SUCCESS = "0";

		/**
		 * 9:異常終了(登録失敗)
		 */
		public static final String ERROR = "9";

	}

	/**
	 * データ区分
	 */
	public class DataKbn {

		/**
		 * 修理受付
		 */
		public static final String SHR = "1";
		/**
		 * ライフバル問い合わせ
		 */
		public static final String LV = "2";
		/**
		 * TES
		 */
		public static final String TES = "3";
		/**
		 * エラーフォロー
		 */
		public static final String EFP = "4";
		/**
		 * 機器見積もり
		 */
		public static final String KIKI_MTMR = "5";
		/**
		 * 修理受付電話
		 */
		public static final String TEL_SHR = "6";
		/**
		 * 水まわり電話受付（PCS）
		 */
		public static final String PCS = "7";
		/**
		 * 水まわりWEB受付
		 */
		public static final String TGL_WEB = "8";
		/**
		 * 水まわり電話受付（TGL/ES）
		 */
		public static final String TGL_TEL = "9";
		/**
		 * 払込書再発行
		 */
		public static final String HK = "A";
		/**
		 * 区分開閉栓
		 */
		public static final String KBN_KHSN = "D";
		/**
		 * ずっともGB
		 */
		public static final String ZGB = "E";
		/**
		 * ガス機器・水まわり修理WEB受付
		 */
		public static final String GW_WEB = "F";

	}

	/**
	 * TES修理フラグ 定数クラス
	 */
	public class TesRepairFlg {
		/**
		 * 一般修理
		 */
		public static final String COMMON_REPAIR = "0";
		/**
		 * TES修理
		 */
		public static final String TES_REPAIR = "1";
	}

	/** LIVALIT連携用定数クラス */
	public class Livalit {

		/** LIVALIT連携 開始年月日時刻 */
		public static final String RENKEI_START_YMD_JKK = "20210815160000";

		/** LIVALIT連携 表示文言用クラス */
		public class Renkei {

			/** 連携済 */
			public static final String COMPLETE = "連携済";

			/** 未連携 */
			public static final String NOT = "未連携";
		}

		/** ステータスコード */
		public class StatusCode {

			/** 200：正常 */
			public static final String CODE_200 = "200";

			/** 400：必須項目設定不足 */
			public static final String CODE_400 = "400";

			/** 404：取得レコードなし */
			public static final String CODE_404 = "404";

			/** 405：データ不正 */
			public static final String CODE_405 = "405";

			/** 406：余力情報検索エラー */
			public static final String CODE_406 = "406";

			/** 407：代替店取得 */
			public static final String CODE_407 = "407";

			/** 408：余力オーバ― */
			public static final String CODE_408 = "408";

			/** 500：そのほかのエラー */
			public static final String CODE_500 = "500";

			/** 10：利用時間外 */
			public static final String CODE_10 = "10";
		}
	}

	/** PRECO対応用定数クラス */
	public class Preco {

		/** 都度払い受付 */
		public static final String IS_PAY_EACH_TIME_TRUE = "1";

		/** PRECO対応 受付経路文言クラス */
		public class InboundRoute {

			/** WEB */
			public static final String INBOUND_ROUTE_WEB = "Web";

		}

		/** PRECO対応 応募辞退区分クラス */
		public class ApplyDeclineKbn {

			/** 応募 */
			public static final String APPLY_KBN = "1";

			/** 辞退 */
			public static final String DECLINE_KBN = "2";

		}

		/** PRECO対応 応募辞退APIレスポンスステータスクラス */
		public class ApplyDeclineStatus {

			/** 成功 */
			public static final String STATUS_SUCCESS = "SUCCESS";

			/** 失敗 */
			public static final String STATUS_FAILURE =  "FAILURE";

		}

		/** PRECO対応 PCS回答列表示文言クラス */
		public class PCSAnswer {

			/** 募集中 */
			public static final String UNDER_RECRUITMENT = "募集中";

			/** 応募済 */
			public static final String APPLIED = "応募済";

			/** 辞退済 */
			public static final String DECLINED = "辞退済";

			/** 未回答 */
			public static final String NOT_ANSWERED = "未回答";
		}

		/** PRECO対応 処理種別クラス */
		public class ProcessKbn {

			/** 「1：差配時」 */
			public static final String KBN_1 = "1";

			/** 「2：応募時」 */
			public static final String KBN_2 = "2";

			/** 「3：手動手配時」 */
			public static final String KBN_3 = "3";
		}

		/** PRECO対応 ステータスクラス */
		public class Status {

			/** 手動手配 */
			public static final String MANUAL = "手動手配";

			/** 出動 */
			public static final String DISPATCH = "出動";
		}

		/** PRECO対応 LIVALIT作業対象区分クラス */
		public class LivalitSgyTsyKbn {

			/** 1A */
			public static final String VALUE_1A = "1A";
			/** 1B */
			public static final String VALUE_1B = "1B";
			/** 1C */
			public static final String VALUE_1C = "1C";

		}
	}

	/** 電話受付用定数クラス */
	public class TelUketuke {

		/** 電話対応完了 */
		public class CompTelSupport {

			/** 1：電話のみで対応完了 */
			public static final String CODE_1 = "1";
		}
	}

}
