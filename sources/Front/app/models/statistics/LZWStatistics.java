package models.statistics;

import models.CompressionAlgorithmType;
import models.FileInfo;

import javax.persistence.*;
import services.statistics.AlgorithmType;
import services.statistics.StatisticAccessor;

@Entity
@Table(name = "LZWStatistics")
@AlgorithmType(algorithmType = CompressionAlgorithmType.lzw)
public class LZWStatistics extends CompressionStatsBase implements ICompressionStats {
	
	private String id;
	private long numberOfFactors;
	private long compressingTime;
	private FileInfo fileInfo;

	public LZWStatistics(){}
	
	public LZWStatistics(String id, long numberOfFactors, long compressingTime, FileInfo fileInfo) {
		this.id = id;
		this.numberOfFactors = numberOfFactors;
		this.compressingTime = compressingTime;
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

	@OneToOne(cascade = CascadeType.ALL)
	public FileInfo getFileInfo() {
		return this.fileInfo;
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	@Column(name="numberOfFactors")
	@StatisticAccessor(statisticType = StatisticType.factorizationSize)
	public long getNumberOfFactors() {
		return numberOfFactors;
	}

	public void setNumberOfFactors(long numberOfFactors) {
		this.numberOfFactors = numberOfFactors;
	}

	@Column(name="compressingTime")
	@StatisticAccessor(statisticType = StatisticType.executionTime)
	public long getCompressingTime() {
		return compressingTime;
	}

	public void setCompressingTime(long compressingTime) {
		this.compressingTime = compressingTime;
	}

	@StatisticAccessor(statisticType = StatisticType.inputSize)	
	public long textSize(){
		return this.fileInfo.getFileSize();
	}
	
	@Override
	@StatisticAccessor(statisticType = StatisticType.compressionRate)
	public float calcCompressionRate() {
		return calcCompressionRate(this.fileInfo.getFileSize(), numberOfFactors);
	}
}
