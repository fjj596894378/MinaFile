package com.minafile.model;

/**
 * 配置文件的model类。
 * @author king_fu
 *
 */
public class PropertiesModel {
	private String serverFilePath; // 服务器保存文件路径
	private String clientFilePath; // 服务器保存文件路径
	private String clientFileName; // 服务器保存文件路径

	public String getServerFilePath() {
		return serverFilePath;
	}

	public void setServerFilePath(String serverFilePath) {
		this.serverFilePath = serverFilePath;
	}

	public String getClientFilePath() {
		return clientFilePath;
	}

	public void setClientFilePath(String clientFilePath) {
		this.clientFilePath = clientFilePath;
	}

	public String getClientFileName() {
		return clientFileName;
	}

	public void setClientFileName(String clientFileName) {
		this.clientFileName = clientFileName;
	}
}
