import java.util.Random;

public class Population {
	
	public static int NP = 20;	// 种群规模
	public static int size = 2;	// 个体长度
	public static int xMin = -4;	// 最小值
	public static int xMax = 4;	// 最大值
	public static double F = 0.5;	// 变异的控制参数
	public static double CR = 0.5;	// 杂交的控制参数
	
	public static double e = Math.E;
	
	private double X[][] = new double[NP][size];			// 个体
	private double XMutation[][] = new double[NP][size];	// 变异后的个体
	private double XCrossOver[][] = new double[NP][size];	// 交叉后的个体
	private double fitness_X[] = new double[NP];	//适应值
	
	public double[][] getX() {
		return X;
	}
	
	/*
	 * 矩阵复制
	 */
	public void setX(double x[][]) {
		for (int i = 0; i < NP; i++) {
			for (int j = 0; j < size; j++) {
				this.X[i][j] = x[i][j];
			}
		}
	}
	
	public double[] getFitness_X() {
		return fitness_X;
	}
	
	public void setFitness_X(double[] fitness_X) {
		for (int i = 0; i < NP; i++) {
			this.fitness_X[i] = fitness_X[i];
		}
	}
	
	public double[][] getXMutation() {
		return XMutation;
	}
	
	public void setXMutation(double XMutation[][]) {
		for (int i = 0; i < NP; i++) {
			for (int j = 0; j < size; j++) {
				this.XMutation[i][j] = XMutation[i][j];
			}
		}
	}
	
	public double[][] getXCrossOver() {
		return XCrossOver;
	}
	
	public void setXCrossOver(double XCrossOver[][]) {
		for (int i = 0; i < NP; i++) {
			for (int j = 0; j < size; j++) {
				this.XCrossOver[i][j] = XCrossOver[i][j];
			}
		}
	}
	
	/*
	 * 适应度的计算
	 * 
	 * @param XTemp根据个体计算适应度
	 * @return 返回适应度
	 */
	public double CalculateFitness(double XTemp[]) {
		double fitness = 0;
		fitness = -20 * Math.pow(e, (-0.2*Math.sqrt((Math.pow(XTemp[0], 2) 
				+ Math.pow(XTemp[1], 2)) / 2)))
				- Math.pow(e, (Math.cos(Math.PI * 2 * XTemp[0])
				+ Math.cos(Math.PI * 2 * XTemp[1])) / 2) + 20 + e;
		return fitness;
	}
	
	/*
	 *初始化种群
	 */
	public void Initialize() {
		double XTemp[][] = new double[NP][size];
		double FitnessTemp[] = new double[NP];
		Random r = new Random();
		for (int i = 0; i < NP; i++) {
			for (int j = 0; j < size; j++) {
				XTemp[i][j] = xMin + r.nextDouble() * (xMax - xMin);
			}
			// 计算适应度
			FitnessTemp[i] = CalculateFitness(XTemp[i]);
		}
		this.setX(XTemp);
		this.setFitness_X(FitnessTemp);
	}
	
	/*
	 * 变异
	 */
	public void Mutation() {
		double XTemp[][] = new double[NP][size];
		double XMutationTemp[][] = new double[NP][size];
		XTemp = this.getX();
		Random r = new Random();
		for (int i = 0; i < NP; i++) {
			int r1 = 0;
			int r2 = 0;
			int r3 = 0;
			
			while (r1 == i || r2 == i || r3 == i || r1 == r2
					|| r1 == r3 || r2 == r3) {
				r1 = r.nextInt(NP);
				r2 = r.nextInt(NP);
				r3 = r.nextInt(NP);
			}
			
			for (int j = 0; j < size; j++) {
				XMutationTemp[i][j] = XTemp[r1][j] + 
						F * (XTemp[r2][j] - XTemp[r3][j]);
			}
		}
		this.setXMutation(XMutationTemp);
	}
	
	/*
	 * 交叉
	 */
	public void CrossOver() {
		double XTemp[][] = new double[NP][size];
		double XMutationTemp[][] = new double[NP][size];
		double XCrossOverTemp[][] = new double[NP][size];
		
		XTemp = this.getX();
		XMutationTemp = this.getXMutation();
		Random r = new Random();
		
		for (int i = 0; i < NP; i++) {
			for (int j = 0; j < size; j++) {
				double rTemp = r.nextDouble();
				if (rTemp <= CR) {
					XCrossOverTemp[i][j] = XMutationTemp[i][j];
				}
				else {
					XCrossOverTemp[i][j] = XTemp[i][j];
				}
			}
		}
		this.setXCrossOver(XCrossOverTemp);
	}
	
	/*
	 * 选择 使用贪婪选择策略
	 */
	public void Selection() {
		double XTemp[][] = new double[NP][size];
		double XCrossOverTemp[][] = new double[NP][size];
		double FitnessTemp[] = new double[NP];
		double FitnessCrossOverTemp[] = new double[NP];
		
		XTemp = this.getX();
		XCrossOverTemp = this.getXCrossOver();
		FitnessTemp = this.getFitness_X();
		
		// 对群体重新设置
		for (int i = 0; i < NP; i++) {
			FitnessCrossOverTemp[i] = CalculateFitness(XCrossOverTemp[i]);
			if (FitnessCrossOverTemp[i] < FitnessTemp[i]) {
				for (int j = 0; j < size; j++) {
					XTemp[i][j] = XCrossOverTemp[i][j];
				}
				FitnessTemp[i] = FitnessCrossOverTemp[i];
			}
		}
		this.setX(XTemp);
		this.setFitness_X(FitnessTemp);
	}
	
	/**
	 * 保存每一代的全局最优值
	 */
	public void SaveBest() {
		double FitnessTemp[] = new double[NP];
		FitnessTemp = this.getFitness_X();
		int temp = 0;
		
		// 找最小值
		for (int i = 1; i < NP; i++) {
			if (FitnessTemp[temp] > FitnessTemp[i]) {
				temp = i;
			}
		}
		System.out.println(FitnessTemp[temp]);
	}
}
