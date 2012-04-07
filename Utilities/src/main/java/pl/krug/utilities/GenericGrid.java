/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.utilities;

/**
 * Type safe generic two dimensional array.
 *
 * @author Edhendil
 */
public class GenericGrid<T> {

    private Object[][] grid;
    private int height;
    private int width;

    public GenericGrid(int width, int height) {
        this.height = height;
        this.width = width;
        grid = new Object[width][height];
    }

    public void setElement(int width, int height, T element) {
        grid[width][height] = element;
    }

    public T getElement(int width, int height) {
        return (T) grid[width][height];
    }

    public void clear() {
        grid = new Object[width][height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
