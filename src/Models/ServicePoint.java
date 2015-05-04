package Models;


import Engine.GeneticAlgorithm;
import Engine.Partials;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServicePoint {

	private String name;
	private Point position;
	private Integer required;
	private HashMap<Factory,Integer> prodReceipts;
	private ArrayList<Factory> factories;

    public ServicePoint(String s, Point cords, Integer value) {
        this.name = s;
        this.position = cords;
        this.required = value;
    }


    public ArrayList<Factory> getFactories() {
		return factories;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Integer getRequired() {
		return required;
	}

	public void setRequired(Integer required) {
		this.required = required;
	}

	public HashMap<Factory, Integer> getProdReceipts() {
		return prodReceipts;
	}

	public void setProdReceipts(HashMap<Factory, Integer> prodReceipts) {
		this.prodReceipts = prodReceipts;
	}

	public ServicePoint(String name, Point position, Integer required,HashMap<Factory,Integer> prod) {
		this.name = name;
		this.position = position;
		this.required = required;
		this.prodReceipts = prod;
	}

	/**
	 * Depending of the quantity received between the different factories generates a specific string
	 * @return the string of a specific service point
	 */
	public String getBinaryRepresentation(Integer representationNumber) {
		String representation = "";

		Iterator it = prodReceipts.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();

			Integer tempProduction = (Integer) pair.getValue();
			representation+= Partials.integerModified(tempProduction, representationNumber);

		}

		return representation;
	}

	public double distanceToFactory(Factory factory) {
		Point p1 = factory.getPosition();
		return Math.sqrt(Math.pow((position.getX() - p1.getX()), 2) + Math.pow((position.getY() - p1.getY()), 2));
	}

    public void setFactories(ArrayList<Factory> factories) {
        this.factories = factories;
    }
}
