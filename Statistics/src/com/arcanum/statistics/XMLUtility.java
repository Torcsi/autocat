package com.arcanum.statistics;

public class XMLUtility {
	static String getXmlName(String name)
	{
		char[] a = name.toCharArray();
		char[] b = new char[a.length+1];
		int l=a.length;
		int i,j;
		boolean wasSpace = true;
		for(i=0,j=0;i<l;i++){
			char c = a[i];
			switch(c){
			case ' ':
			case '_':
				if( wasSpace )
					continue;
				b[j++] = '_';
				wasSpace = true;
				break;
			case ':':
				b[j++] = ':';
				break;
			default:
				if( Character.isDigit(c) || c=='-'){
					if( j == 0){
						b[j++] = '_';
					}else{
						b[j++] = c;
					}
					wasSpace = false;
				}else if( Character.isAlphabetic(c) ){
					b[j++] = c;
					wasSpace = false;
				}
			}
		}
		return new String(b,0,j);
	}

}
