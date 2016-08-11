package com.bfsi.egalite.entity;

public class CashRowItems {
    private String ccycode;
    private String opencase;
    private String cashin;
    private String cashout;
    private String presentcase;
 
    /*public RowItem(String title) {
        this.title = title;  
    }*/
    public CashRowItems(String ccycode, String opencase, String cashin, 
    		String cashout, String presentcase) {
        this.ccycode = ccycode;
        this.opencase = opencase;
        this.cashin = cashin;
        this.cashout = cashout;
        this.presentcase = presentcase;
    }
    
    public String getCcycode() {
		return ccycode;
	}

	public void setCcycode(String ccycode) {
		this.ccycode = ccycode;
	}

	public String getOpencase() {
		return opencase;
	}

	public void setOpencase(String opencase) {
		this.opencase = opencase;
	}

	public String getCashin() {
		return cashin;
	}

	public void setCashin(String cashin) {
		this.cashin = cashin;
	}

	public String getCashout() {
		return cashout;
	}

	public void setCashout(String cashout) {
		this.cashout = cashout;
	}

	public String getPresentcase() {
		return presentcase;
	}

	public void setPresentcase(String presentcase) {
		this.presentcase = presentcase;
	}

	@Override
    public String toString() {
        return ccycode + "\n" + opencase+ "\n" + cashin+ "\n" + cashout+ "\n" + presentcase;
    }
}

