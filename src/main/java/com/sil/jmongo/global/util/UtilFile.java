package com.sil.jmongo.global.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 파일유틸
 */
@Slf4j
public class UtilFile {
	
	/**
	 * 상위폴더 모두 생성
	 * @param path
	 */
	public static void makeFolders(String path) {
		File Folder = new File(path);
		// 해당 디렉토리가 없다면 디렉토리를 생성.
		if (!Folder.exists()) {
			try{
				Folder.mkdirs(); // 상위폴더 모두 생성
				log.debug("폴더생성완료: {}", path);
				} 
				catch(Exception e){
				e.getStackTrace();
			}		
		}
	}
	
}
