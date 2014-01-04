package it.sblt;

public class Numbr {
	public String name;
	public int value;

	public Numbr(String n, int v) {
		name = n;
		value = v;
	}
	
	@Override
	public String toString() {
		return name + "=" + value;
	}
}
