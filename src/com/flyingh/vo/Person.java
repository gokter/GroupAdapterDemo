package com.flyingh.vo;

public class Person implements Comparable<Person> {
	private String name;
	private int age;
	private float score;

	public Person() {
		super();
	}

	public Person(String name, int age, float score) {
		super();
		this.name = name;
		this.age = age;
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", score=" + score + "]";
	}

	@Override
	public int compareTo(Person another) {
		return Float.compare(score, another.score);
	}
}
