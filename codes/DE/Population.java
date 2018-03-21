import java.util.Random;

public class Population {
	
	public static int NP = 20;	// ��Ⱥ��ģ
	public static int size = 2;	// ���峤��
	public static int xMin = -4;	// ��Сֵ
	public static int xMax = 4;	// ���ֵ
	public static double F = 0.5;	// ����Ŀ��Ʋ���
	public static double CR = 0.5;	// �ӽ��Ŀ��Ʋ���
	
	public static double e = Math.E;
	
	private double X[][] = new double[NP][size];			// ����
	private double XMutation[][] = new double[NP][size];	// �����ĸ���
	private double XCrossOver[][] = new double[NP][size];	// �����ĸ���
	private double fitness_X[] = new double[NP];	//��Ӧֵ
	
	public double[][] getX() {
		return X;
	}
	
	/*
	 * ������
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
	 * ��Ӧ�ȵļ���
	 * 
	 * @param XTemp���ݸ��������Ӧ��
	 * @return ������Ӧ��
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
	 *��ʼ����Ⱥ
	 */
	public void Initialize() {
		double XTemp[][] = new double[NP][size];
		double FitnessTemp[] = new double[NP];
		Random r = new Random();
		for (int i = 0; i < NP; i++) {
			for (int j = 0; j < size; j++) {
				XTemp[i][j] = xMin + r.nextDouble() * (xMax - xMin);
			}
			// ������Ӧ��
			FitnessTemp[i] = CalculateFitness(XTemp[i]);
		}
		this.setX(XTemp);
		this.setFitness_X(FitnessTemp);
	}
	
	/*
	 * ����
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
	 * ����
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
	 * ѡ�� ʹ��̰��ѡ�����
	 */
	public void Selection() {
		double XTemp[][] = new double[NP][size];
		double XCrossOverTemp[][] = new double[NP][size];
		double FitnessTemp[] = new double[NP];
		double FitnessCrossOverTemp[] = new double[NP];
		
		XTemp = this.getX();
		XCrossOverTemp = this.getXCrossOver();
		FitnessTemp = this.getFitness_X();
		
		// ��Ⱥ����������
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
	 * ����ÿһ����ȫ������ֵ
	 */
	public void SaveBest() {
		double FitnessTemp[] = new double[NP];
		FitnessTemp = this.getFitness_X();
		int temp = 0;
		
		// ����Сֵ
		for (int i = 1; i < NP; i++) {
			if (FitnessTemp[temp] > FitnessTemp[i]) {
				temp = i;
			}
		}
		System.out.println(FitnessTemp[temp]);
	}
}
