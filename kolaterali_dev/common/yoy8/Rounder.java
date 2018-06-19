/*
 * Created on 2007.04.13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import hr.vestigo.modules.rba.util.DecimalUtils;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author hraamh
 *
 * Klasa za zaokruzivanje i nastimavanje zbroj zaokruzenih vrijednosti
 */
public class Rounder {
	
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/Rounder.java,v 1.1 2007/05/02 14:24:26 hraamh Exp $";
	
	private final int BALANCED=0;	
	private final int MINORED=1;	
	private final int MAJORED=2;	
	private final int IRRELEVANT=3;	
	private final int ERROR=4;
	
	private int[] columnStates=null;
	private int[] rowStates=null;

	private long[][] values=null;
	
	private long[] columnValues=null;	
	private long[] rowValues=null;
	
	private long[] columnSum=null;
	private long[] rowSum=null;
	
	private boolean[][] flags;
	
	private int roundPrecision=2;
	
	private int roundError=5;
	
	public Rounder(BigDecimal[][] values, BigDecimal[]columns, BigDecimal[] rows, int roundPrecision, int roundError) {
		this.values=new long[values.length][values[0].length];
		this.flags=new boolean[values.length][values[0].length];
		
		this.rowValues= new long[values.length];
		this.columnValues= new long[values[0].length];
		
		this.rowSum= new long[values.length];
		Arrays.fill(rowSum,0);
		this.columnSum= new long[values[0].length];
		Arrays.fill(columnSum,0);
		
		this.rowStates= new int[values.length];
		this.columnStates= new int[this.values[0].length];
		
		this.roundPrecision=roundPrecision;
		this.roundError=roundError;
		
		int maxDimension;
		if(rows.length>columns.length){
			maxDimension=rows.length;
		}else{
			maxDimension=columns.length;
		}	
		//u jednoj petlji postavljam zaokruzene vrijednosti plasmana i kolaterala da bi izbjegao 2 petlje = BRZE!
		for(int i=0;i<maxDimension;i++){
			if(rows.length>i){
				this.rowValues[i]=(DecimalUtils.scale(rows[i],this.roundPrecision)).unscaledValue().longValue();
			}
			if(columns.length>i){
				this.columnValues[i]=(DecimalUtils.scale(columns[i],this.roundPrecision)).unscaledValue().longValue();
			}
		}
		
		for(int i=0;i<values.length;i++){
			this.rowValues[i]=(DecimalUtils.scale(rows[i],this.roundPrecision)).unscaledValue().longValue();
			for(int j=0;j<values[0].length;j++){
				if(values[i][j]!=null){
					this.values[i][j]= (DecimalUtils.scale(values[i][j],this.roundPrecision)).unscaledValue().longValue();
					this.flags[i][j]=true;
					rowSum[i]+=this.values[i][j];
					columnSum[j]+=this.values[i][j];
				}	
			}
		}
		
		for(int i=0;i<maxDimension;i++){
			if(rowSum.length>i){
				checkRow(i);
			}
			if(columnSum.length>i){
				checkColumn(i);
			}
		}
	}
	
	private void checkRow(int index){
		long difference=this.rowValues[index]-this.rowSum[index];
		if(difference>5){
			rowStates[index]=IRRELEVANT;
		}else if((difference<=5)&&(difference>0)){
			rowStates[index]=MINORED;
		}else if(difference==0){
			rowStates[index]=BALANCED;
		}else if((difference<0)&&(-5<=difference)){
			rowStates[index]=MAJORED;
		}else if(difference<-5){
			rowStates[index]=ERROR;
		}
	}
	
	private void checkColumn(int index){
		long difference=this.columnValues[index]-this.columnSum[index];
		if(difference>5){
			columnStates[index]=IRRELEVANT;
		}else if((difference<=5)&&(difference>0)){
			columnStates[index]=MINORED;
		}else if(difference==0){
			columnStates[index]=BALANCED;
		}else if((difference<0)&&(-5<=difference)){
			columnStates[index]=MAJORED;
		}else if(difference<-5){
			columnStates[index]=ERROR;
		}
	}
	
	private long sumColumn(int index){
		long result= 0;
		for(int i=0;i<values.length;i++){
			if(flags[i][index]){
				result+=result;
			}
		}
		return result;
	}
	
	private long sumRow(int index){
		long result= 0;
		for(int i=0;i<values[0].length;i++){
			if(flags[index][i]){
				result+=result;
			}
		}
		return result;
	}
	
	private BigDecimal[][] toDecimalValue(){
		BigDecimal[][] result=new BigDecimal[values.length][values[0].length];
		for(int i=0;i<values.length;i++)
			for(int j=0;j<values[0].length;j++){
				if(flags[i][j]){
					result[i][j]= new BigDecimal(values[i][j]).movePointLeft(this.roundPrecision);
				}
			}
		return result;
	}
	
	public void print(){
		System.out.print("PL\\COLL\t");
		for(int i=0;i<this.columnValues.length;i++){
			System.out.print("\t| "+new BigDecimal(columnValues[i]).movePointLeft(this.roundPrecision));
		}
		System.out.println();
		System.out.println("\t\t-------------------------------------------------------------");
		for(int i=0;i<this.values.length;i++){
			long temp=0;
			System.out.print(new BigDecimal(rowValues[i]).movePointLeft(this.roundPrecision));
			for(int j=0;j<this.values[0].length;j++){
				if(flags[i][j]){
					System.out.print("\t\t| "+new BigDecimal(values[i][j]).movePointLeft(this.roundPrecision));
					temp+=values[i][j];
				}else{
					System.out.print("\t\t| X");
				}
				
			}
			System.out.print("\t\t|"+new BigDecimal(temp).movePointLeft(this.roundPrecision));
			switch(rowStates[i]){
				case 0:
					System.out.println("\t\t BALANCED");
					break;
				case 1:
					System.out.println("\t\t MINORED");
					break;
				case 2:
					System.out.println("\t\t MAJORED");
					break;
				case 3:
					System.out.println("\t\t IRRELEVANT");
					break;
				case 4:
					System.out.println("\t\t ERROR");
					break;
			}
		}
		System.out.println("\t\t-------------------------------------------------");
		for(int j=0;j<this.values[0].length;j++){
			System.out.print("\t\t| "+new BigDecimal(columnSum[j]).movePointLeft(this.roundPrecision));
		}
		System.out.println();
		System.out.print("\t");
		for(int i=0;i<this.columnValues.length;i++){
			switch(columnStates[i]){
			case 0:
				System.out.print("\t BALANCED");
				break;
			case 1:
				System.out.print("\t MINORED");
				break;
			case 2:
				System.out.print("\t MAJORED");
				break;
			case 3:
				System.out.print("\t IRRELEVANT");
				break;
			case 4:
				System.out.print("\t ERROR");
				break;
			}
		}
		System.out.println();
	}
	
	public BigDecimal[][] fix(){
		print();
		for(int i=0;i<this.rowStates.length;i++){
			if(rowStates[i]==MINORED){
				fixRow(i,MINORED);
				System.out.println();
				print();
				System.out.println();
			}else if(rowStates[i]==MAJORED){
				fixRow(i,MAJORED);
				System.out.println();
				print();
				System.out.println();
			}			
		}	
		for(int i=0;i<this.rowStates.length;i++){
			if(rowStates[i]==MINORED){
				fixRow(i,IRRELEVANT);
				System.out.println();
				print();
				System.out.println();
			}else if(rowStates[i]==MAJORED){
				fixRow(i,IRRELEVANT);
				System.out.println();
				print();
				System.out.println();
			}	
		}
		for(int i=0;i<this.rowStates.length;i++){
			if(rowStates[i]==MINORED){
				fixRow(i,BALANCED);
				System.out.println();
				print();
				System.out.println();
			}else if(rowStates[i]==MAJORED){
				fixRow(i,BALANCED);
				System.out.println();
				print();
				System.out.println();
			}	
		}
		print();
		return toDecimalValue();
	}
	
	private void fixRow(int rowIndex, int columnState){
		for(int i=0;i<columnStates.length;i++){
			int state=rowStates[rowIndex];
			if(state==BALANCED) return;
			if((columnStates[i]==columnState)&&(flags[rowIndex][i])){
				long fixValue=0;
				if(columnState!=BALANCED){
					fixValue=Math.min(Math.abs(rowValues[rowIndex]-rowSum[rowIndex]),Math.abs(columnValues[i]-columnSum[i]));
				}else{
					fixValue=Math.abs(rowValues[rowIndex]-rowSum[rowIndex]);
				}
				
				if(state==MINORED){
					values[rowIndex][i]+=fixValue;
					rowSum[rowIndex]+=fixValue;
					columnSum[i]+=fixValue;
					checkRow(rowIndex);
					checkColumn(i);
				}else if(state==MAJORED){
					values[rowIndex][i]-=fixValue;
					rowSum[rowIndex]-=fixValue;
					columnSum[i]-=fixValue;
					checkRow(rowIndex);
					checkColumn(i);
				}			
			}
		}
	}
	
	/**
	 * @return Vraca zaokruzene predane vrijednosti kolona
	 */
	public BigDecimal[] getColumnValues() {
		BigDecimal[] result = new BigDecimal[columnValues.length];
		for(int i=0;i<columnValues.length;i++){
			new BigDecimal(columnValues[i]).movePointLeft(this.roundPrecision);
		}
		return result;
	}
	/**
	 * @return Vraca zaokruzene predane vrijednosti redaka
	 */
	public BigDecimal[] getRowValues() {
		BigDecimal[] result = new BigDecimal[rowValues.length];
		for(int i=0;i<rowValues.length;i++){
			new BigDecimal(rowValues[i]).movePointLeft(this.roundPrecision);
		}
		return result;
	}

}
