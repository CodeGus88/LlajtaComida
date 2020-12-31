package com.appcocha.llajtacomida.presenters.tools;

import com.appcocha.llajtacomida.models.plate.Plate;
import com.appcocha.llajtacomida.presenters.plate.PlateList;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

public class ValidationTest extends TestCase {
    private ArrayList<Plate> plateList;
    public ValidationTest() {
        plateList = new ArrayList<Plate>();
        Plate plate = new Plate("1");
        plate.setName("Pique Macho");
        Plate plate2 = new Plate("2");
        plate2.setName("Sopa de Maní");
        plateList.add(plate);
        plateList.add(plate2);
        PlateList plateListStatic = new PlateList(plateList);
    }

    @Test
    public void testIsEmpty() {
        assertEquals(true, Validation.isEmpty("    "));
    }

    @Test
    public void testIsNotEmpty() {
        assertEquals(false, Validation.isEmpty("  Contiene texto  "));
    }

    @Test
    public void testCorrectText() {
        assertEquals("este es un texto", Validation.correctText("   este     es   un texto"));
    }

    @Test
    public void testIsPhone() {
        assertEquals(true, Validation.isPhone("4385632"));
    }

    @Test
    public void testIsNotPhone(){
        assertEquals(false, Validation.isPhone("385632"));
    }

    @Test
    public void testIsMovilPhone() {
        assertEquals(true, Validation.isPhone("79385475"));
    }

    @Test
    public void testIsNotMovilPhone(){
        assertEquals(false, Validation.isPhone("7938547"));
    }

    @Test
    public void testIsMultiPhone(){
        assertEquals(true, Validation.isPhone("4386994,79856412"));
    }

    @Test
    public void testIsNotMultiPhone(){
        assertEquals(false, Validation.isPhone("438699,7985642"));
    }

    @Test
    public void testGetFirstName() {
        assertEquals("Gustavo", Validation.getFirstName("Gustavo Abasto"));
    }

    @Test
    public void testExistPlateName() {
        assertEquals(true, Validation.existPlateName("Pique Macho"));
    }

    @Test
    public void testNoExistPlateName() {
        assertEquals(false, Validation.existPlateName("Plate"));
    }

    @Test
    public void testExistPlateNameExcludePlateId() {
        assertEquals(false, Validation.existPlateNameExcludePlateId("2", "Sopa de Maní"));
    }

    @Test
    public void testNoExistPlateNameExcludePlateId() {
        assertEquals(false, Validation.existPlateNameExcludePlateId("2", "Plate"));
    }

    @Test
    public void testExistPlateNameExcludePlateIdX() {
        assertEquals(true, Validation.existPlateNameExcludePlateId("2", "Pique Macho"));
    }


    @Test
    public void testIsPersonName() {
        assertEquals(true, Validation.isPersonName("Gustavo"));
    }

    @Test
    public void testIsNotPersonName() {
        assertEquals(false, Validation.isPersonName("Gustavo1"));
    }
}