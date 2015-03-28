package models.statistics;

import models.CompressionAlgorithmType;
import models.FileInfo;

import javax.persistence.*;
import services.statistics.AlgorithmType;
import services.statistics.StatisticAccessor;

@Entity
@Table(name = "SLPOptimalStatistics")
@AlgorithmType(algorithmType = CompressionAlgorithmType.slpModern)
public class SLPOptimalStatistics extends CompressionStatsBase implements ICompressionStats {

	private String id;
	private long compressedFileSize;
	private long slpBuildingTime;
	private String dataFactoryType;
	private long rebalanceCount;
	private long height;
	private FileInfo fileInfo;

	public SLPOptimalStatistics() {}

	public SLPOptimalStatistics(String id, long copmressedFileSize, long slpBuildingTime,
			String dataFactoryType, int rebalanceCount, long height, FileInfo fileInfo) {
		this.id = id;
		this.compressedFileSize = copmressedFileSize;
		this.slpBuildingTime = slpBuildingTime;
		this.dataFactoryType = dataFactoryType;
		this.rebalanceCount = rebalanceCount;
		this.height = height;
		this.fileInfo = fileInfo;
	}

	@Id
	@Column(name="id", length=50)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name="compressedFileSize")
	public long getCopmressedFileSize() {
		return compressedFileSize;
	}

	public void setCopmressedFileSize(long copmressedFileSize) {
		this.compressedFileSize = copmressedFileSize;
	}

	@Column(name="slpBuildingTime")
	@StatisticAccessor(statisticType = StatisticType.executionTime)
	public long getSlpBuildingTime() {
		return slpBuildingTime;
	}

	public void setSlpBuildingTime(long slpBuildingTime) {
		this.slpBuildingTime = slpBuildingTime;
	}

	@Column(name="dataFactoryType", length = 50)
	public String getDataFactoryType() {
		return dataFactoryType;
	}

	public void setDataFactoryType(String dataFactoryType) {
		this.dataFactoryType = dataFactoryType;
	}

	@Column(name="rebalanceCount")
	@StatisticAccessor(statisticType = StatisticType.avlRotationsNumber)
	public long getRebalanceCount() {
		return rebalanceCount;
	}

	public void setRebalanceCount(long rebalanceCount) {
		this.rebalanceCount = rebalanceCount;
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
	public long fileSize(){
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
	public float calcCompressionRate()
	{
		return calcCompressionRate(fileInfo.getFileSize(), compressedFileSize);
	}
}
