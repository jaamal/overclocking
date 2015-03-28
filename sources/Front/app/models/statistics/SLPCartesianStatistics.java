package models.statistics;

import models.CompressionAlgorithmType;
import models.FileInfo;

import javax.persistence.*;
import services.statistics.AlgorithmType;
import services.statistics.StatisticAccessor;

@Entity
@Table(name="SLPCartesianStatistics")
@AlgorithmType(algorithmType = CompressionAlgorithmType.slpCartesian)
public class SLPCartesianStatistics extends CompressionStatsBase implements ICompressionStats {

	private String id;
	private long slpBuildingTime;
    private long length;
    private long countRules;
    private long height;
	private FileInfo fileInfo;

	public SLPCartesianStatistics(){}
	
	public SLPCartesianStatistics(String id, long slpBuildingTime, long length, long countRules, long height, FileInfo fileInfo) {
		this.id = id;
		this.slpBuildingTime = slpBuildingTime;
		this.length = length;
		this.countRules = countRules;
		this.height = height;
		this.fileInfo = fileInfo;
	}

	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "slpBuildingTime")
	@StatisticAccessor(statisticType = StatisticType.executionTime)
	public long getSlpBuildingTime() {
		return slpBuildingTime;
	}

	public void setSlpBuildingTime(long slpBuildingTime) {
		this.slpBuildingTime = slpBuildingTime;
	}

	@Column(name = "length")
	@Deprecated
	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	@Column(name = "countRules")
	public long getCountRules() {
		return countRules;
	}

	public void setCountRules(long countRules) {
		this.countRules = countRules;
	}

	@Column(name = "height")
	@StatisticAccessor(statisticType = StatisticType.slpHeight)
	public long getHeight() {
		return height;
	}

	public void setHeight(long height) {
		this.height = height;
	}
	
	@StatisticAccessor(statisticType = StatisticType.inputSize)
	public long textSize(){
		return this.fileInfo.getFileSize();
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	public FileInfo getFileInfo() {
		return this.fileInfo;
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	@Override
	@StatisticAccessor(statisticType = StatisticType.compressionRate)
	public float calcCompressionRate() {
		return calcCompressionRate(fileInfo.getFileSize(), countRules);
	}
}
