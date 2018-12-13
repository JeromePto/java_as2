package client_server;

public class SmoothRandom {
	private long nbCall;
	private double period;
	private double slopeAt[] = new double[2];
	private long nbSlope;
	
	// work better if the period is not a multiple of 1
	public SmoothRandom(double period) {
		this.nbCall = 0;
		this.nbSlope = 0;
		
		for(int i = 0 ; i < 2 ; i++) {
			slopeAt[i] = (Math.random()*2d)-1d;
		}
		
		if(period <= 0 || period >= 0.5d) {
			this.period = 0.3d;
		}
		else
		{
			this.period = period;
		}
	}
	
	public double nextDouble() {
		
		double x = (double)nbCall * period;
		
		long lo = (long) Math.floor(x);
		
		if(lo > nbSlope) {
			slopeAt[0] = slopeAt[1];
			slopeAt[1] = (Math.random()*2d)-1d;
			nbSlope++;
		}
		
		double dist = x-lo;
		double loSlope = slopeAt[0];
		double hiSlope = slopeAt[1];
		double loPos = loSlope * dist;
		double hiPos = -hiSlope * (1-dist);
		double u = dist*dist*(3d-2d*dist);
		double out = (loPos*(1d-u) + (hiPos*u));
		
		nbCall++;
		
		return out;
	}
	
	public double nextDouble(double min, double max) {
		return rangeToRange(nextDouble(), -0.5d, 0.5d, min, max);
	}
	
	public long nextLong(long min, long max) {
		return Math.round(rangeToRange(nextDouble(), -0.5d, 0.5d, (double)min, (double)max));
	}
	
	public int nextInt(int min, int max) {
		return (int)Math.round(rangeToRange(nextDouble(), -0.5d, 0.5d, (double)min, (double)max));
	}
	
	public float nextFloat(float min, float max) {
		return (float)rangeToRange(nextDouble(), -0.5d, 0.5d, (double)min, (double)max);
	}
	
	private static double rangeToRange(double valueIn, double inMin, double inMax, double outMin, double outMax) {
		double tmp = valueIn;
		tmp = (tmp-inMin) / (inMax-inMin);
		tmp = (tmp * (outMax - outMin)) + outMin;
		return tmp;
	}

}
