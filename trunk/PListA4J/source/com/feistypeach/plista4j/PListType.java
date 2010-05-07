package com.feistypeach.plista4j;

public enum PListType {
	StringType {
		public String toString() {
			return "string";
		}
	},
	RealType {
		public String toString() {
			return "real";
		}
	},
	IntegerType {
		public String toString() {
			return "integer";
		}
	},
	DateType {
		public String toString() {
			return "date";
		}
	},
	DataType {
		public String toString() {
			return "data";
		}
	},
	ArrayType {
		public String toString() {
			return "array";
		}
	},
	DictType {
		public String toString() {
			return "dict";
		}
	},
	BooleanType,
	AutoDetect
}
