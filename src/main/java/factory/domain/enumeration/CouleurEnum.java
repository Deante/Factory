package factory.domain.enumeration;

/**
 * The CouleurEnum enumeration.
 */
public enum CouleurEnum {
    ROUGE, VERT, BLEU, JAUNE, NOIR
    ROUGE(205,0,0), VERT(0,100,0), BLEU(25,25,112), JAUNE(238,201,0), NOIR(255,255,255);
    
    
    private int r;
    private int g;
    private int b;
	
    private CouleurEnum(int r, int g, int b) {
    	this.r = r;
    	this.b = b;
    	this.g = g; 
    }

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}
    
    
}
