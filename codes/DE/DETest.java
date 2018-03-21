
public class DETest {
	public static void main(String argsp[]) {
		int gen = 0;
		int maxCycle = 500;
		Population p = new Population();
		p.Initialize();
		while (gen < maxCycle) {
			p.Mutation();
			p.CrossOver();
			p.Selection();
			gen++;
			p.SaveBest();
		}
	}
}
