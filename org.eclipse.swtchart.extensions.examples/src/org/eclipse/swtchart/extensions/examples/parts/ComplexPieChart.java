/*******************************************************************************
 * Copyright (c) 2020, 2024 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta - initial API and implementation
 * Philip Wenig - series settings mappings
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.parts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.piecharts.CircularSeriesData;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesData;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;
import org.eclipse.swtchart.extensions.piecharts.PieChart;

import jakarta.inject.Inject;

public class ComplexPieChart extends PieChart {

	private static final String WITHOUT_CLASSIFICATION = "Without Classification";
	private static final String SECTOR_GICS = "Sector GICS";
	private static final String HEALTHCARE = "Healthcare";
	private static final String HEALTHCARE_SERVICES_1 = "Healthcare Services 1";
	private static final String HEALTHCARE_SERVICES_2 = "Healthcare Services 2";
	private static final String HEALTHCARE_SERVICES_3 = "Healthcare Services 3";
	private static final String CUSTOMER_GOODS = "Customer Goods";
	private static final String INDUSTRY = "Industry";
	private static final String INFORMATION_TECHNOLOGY = "Information Technology";
	private static final String PUBLIC_RELATIONS = "Public Relations";
	private static final String TELECOMMUNICATIONS = "Telecommunications";
	private static final String LUXURY_GOODS = "Luxury Goods";
	private static final String FINANCIAL = "Financial";
	private static final String BASIC_GOODS = "Basic Goods";
	private static final String PUBLIC_UTILITY_1 = "Public Utility 1";
	private static final String PUBLIC_UTILITY_2 = "Public Utility 2";
	private static final String PUBLIC_MULTI_UTILITY_1 = "Multi Utility 1";
	private static final String PUBLIC_MULTI_UTILITY_2 = "Multi Utility 2";
	private static final String TELECOMMUNICATIONS_DIVERSE = "Telecommunications Diverse";
	private static final String TELECOMMUNICATIONS_INTEGRATED = "Telecommunications Integrated";
	private static final String SEMICONDUCTOR_PRODUCTION_1 = "Semiconductor Production 1";
	private static final String SEMICONDUCTOR_PRODUCTION_2 = "Semiconductor Production 2";
	private static final String SEMICONDUCTOR = "Semiconductor";
	private static final String SOFTWARE_SERVICES = "Software Services";
	private static final String SOFTWARE = "Software";
	private static final String SOFTWARE_APPLICATIONS = "Software Applications";
	private static final String BANK_1 = "Bank 1";
	private static final String BANK_2 = "Bank 2";
	private static final String BANK_GENERAL = "Bank General";
	private static final String REAL_ESTATE = "Real Estate";
	private static final String INSURANCE_1 = "Insurance 1";
	private static final String INSURANCE_2 = "Insurance 2";
	private static final String FINANCIAL_SERVICES_2 = "Financial Services 2";
	private static final String REAL_ESTATE_BASIS = "Real Estate Basis";
	private static final String REAL_ESTATE_ACTIVITIES = "Real Estate Activities";
	private static final String ACCIDENT_INSURANCE = "Accident Insurance";
	private static final String REINSURANCE = "Reinsurance";
	private static final String CAPITAL_MARKET = "Capital Market";
	private static final String FINANCIAL_SERVICES_DIVERSE = "Financial Services Diverse";
	private static final String FINANCIAL_SERVICES_SPECIALIZED = "Financial Services Specialized";
	private static final String CUSTODIAN = "Custodian";
	private static final String SCENT_AND_CARE_1 = "Scent and Care 1";
	private static final String SCENT_AND_CARE_2 = "Scent and Care 2";
	private static final String SCENT_AND_CARE_3 = "Scent and Care 3";
	private static final String CHEMICALS_1 = "Chemicals 1";
	private static final String CHEMICALS_2 = "Chemicals 2";
	private static final String BUILDING_MATERIALS_1 = "Raw Materials 1";
	private static final String BUILDING_MATERIALS_2 = "Raw Materials 2";
	private static final String BUILDING_MATERIALS_3 = "Raw Materials 3";
	private static final String PHARMA_1 = "Pharma 1";
	private static final String PHARMA_2 = "Pharma 2";
	private static final String PHARMA_3 = "Pharma 3";
	private static final String AUTOMOBILE_1 = "Automobile 1";
	private static final String AUTOMOBILE_2 = "Automobile 2";
	private static final String AUTOMOBILE_3 = "Automobile 3";
	private static final String TRANSPORT = "Transport";
	private static final String AVIATION_1 = "Aviation 1";
	private static final String AVIATION_2 = "Aviation 2";
	private static final String AVIATION_3 = "Aviation 3";
	private static final String AVIATION_4 = "Aviation 4";
	private static final String INVESTMENT = "Investment";
	private static final String DEFENSE_1 = "Defense 1";
	private static final String DEFENSE_2 = "Defense 2";
	private static final String INDUSTRY_1 = "Industry 1";
	private static final String INDUSTRY_2 = "Industry 2";
	private static final String INDUSTRY_3 = "Industry 3";
	private static final String INDUSTRY_4 = "Industry 4";
	private static final String RETAIL_1 = "Retail 1";
	private static final String RETAIL_2 = "Retail 2";
	private static final String RETAIL_3 = "Retail 3";
	private static final String RETAIL_4 = "Retail 4";
	private static final String RETAIL_5 = "Retail 5";
	//
	private static final String[] labels1 = {WITHOUT_CLASSIFICATION, SECTOR_GICS};
	private static final double[] values1 = {4446, 32214};
	private static final String[] labels2 = {"Account"};
	private static final double[] values2 = {4446};
	private static final String[] labels3 = {HEALTHCARE, PUBLIC_UTILITY_2, CUSTOMER_GOODS, INDUSTRY, INFORMATION_TECHNOLOGY, PUBLIC_RELATIONS, BASIC_GOODS, LUXURY_GOODS, FINANCIAL};
	private static final double[] values3 = {3816, 3523, 1560, 5831, 2651, 1190, 2132, 5052, 6455};
	private static final String[] labels4 = {PUBLIC_UTILITY_1};
	private static final double[] values4 = {3523};
	private static final String[] labels5 = {PUBLIC_MULTI_UTILITY_2};
	private static final double[] values5 = {3523};
	private static final String[] labels6 = {PUBLIC_MULTI_UTILITY_1};
	private static final double[] values6 = {3523};
	private static final String[] labels7 = {"RWE AG", "E.ON Aktiengesellschaft AG"};
	private static final double[] values7 = {2183, 1340};
	private static final String[] labels8 = {TELECOMMUNICATIONS};
	private static final double[] values8 = {1190};
	private static final String[] labels9 = {TELECOMMUNICATIONS_DIVERSE};
	private static final double[] values9 = {1190};
	private static final String[] labels10 = {TELECOMMUNICATIONS_INTEGRATED};
	private static final double[] values10 = {1190};
	private static final String[] labels11 = {"Deutsche Telekom AG"};
	private static final double[] values11 = {1190};
	private static final String[] labels12 = {SEMICONDUCTOR_PRODUCTION_2, SOFTWARE_SERVICES};
	private static final double[] values12 = {1469, 1182};
	private static final String[] labels13 = {SEMICONDUCTOR_PRODUCTION_1};
	private static final double[] values13 = {1469};
	private static final String[] labels14 = {SEMICONDUCTOR};
	private static final double[] values14 = {1469};
	private static final String[] labels15 = {"Infineon Technologies AG"};
	private static final double[] values15 = {1469};
	private static final String[] labels16 = {SOFTWARE};
	private static final double[] values16 = {1182};
	private static final String[] labels17 = {SOFTWARE_APPLICATIONS};
	private static final double[] values17 = {1182};
	private static final String[] labels18 = {"SAP SE O.N."};
	private static final double[] values18 = {1182};
	private static final String[] labels19 = {BANK_2, REAL_ESTATE, INSURANCE_2, FINANCIAL_SERVICES_2};
	private static final double[] values19 = {907, 1187, 2778, 1583};
	private static final String[] labels20 = {REAL_ESTATE_BASIS};
	private static final double[] values20 = {1187};
	private static final String[] labels21 = {REAL_ESTATE_ACTIVITIES};
	private static final double[] values21 = {1187};
	private static final String[] labels22 = {"VONOVIA SE NA O.N."};
	private static final double[] values22 = {1187};
	private static final String[] labels23 = {INSURANCE_1};
	private static final double[] values23 = {2778};
	private static final String[] labels24 = {ACCIDENT_INSURANCE, REINSURANCE};
	private static final double[] values24 = {1147, 1630};
	private static final String[] labels25 = {"Muenchener Rueckversicherungs AG"};
	private static final double[] values25 = {1630};
	private static final String[] labels26 = {"Allianz SE"};
	private static final double[] values26 = {1147};
	private static final String[] labels27 = {CAPITAL_MARKET, FINANCIAL_SERVICES_DIVERSE};
	private static final double[] values27 = {1577, 6};
	private static final String[] labels28 = {CUSTODIAN};
	private static final double[] values28 = {1577};
	private static final String[] labels29 = {"Deutsche Börse AG"};
	private static final double[] values29 = {1577};
	private static final String[] labels30 = {FINANCIAL_SERVICES_SPECIALIZED};
	private static final double[] values30 = {6};
	private static final String[] labels31 = {"WIRECARD AG"};
	private static final double[] values31 = {6};
	private static final String[] labels32 = {BANK_1};
	private static final double[] values32 = {907};
	private static final String[] labels33 = {BANK_GENERAL};
	private static final double[] values33 = {907};
	private static final String[] labels34 = {"Deutsche Bank AG"};
	private static final double[] values34 = {907};
	private static final String[] labels35 = {HEALTHCARE_SERVICES_1, PHARMA_1};
	private static final double[] values35 = {1282, 2533};
	private static final String[] labels36 = {PHARMA_2};
	private static final double[] values36 = {2533};
	private static final String[] labels37 = {PHARMA_3};
	private static final double[] values37 = {2533};
	private static final String[] labels38 = {"Merck KGaA", "Bayer AG"};
	private static final double[] values38 = {1992, 541};
	private static final String[] labels39 = {HEALTHCARE_SERVICES_2};
	private static final double[] values39 = {1282};
	private static final String[] labels40 = {HEALTHCARE_SERVICES_3};
	private static final double[] values40 = {1282};
	private static final String[] labels41 = {"Fresenius SE & Co KGaA", "Fresenius Medical Care Corporation"};
	private static final double[] values41 = {589, 692};
	private static final String[] labels42 = {SCENT_AND_CARE_1};
	private static final double[] values42 = {1560};
	private static final String[] labels43 = {SCENT_AND_CARE_2};
	private static final double[] values43 = {1560};
	private static final String[] labels44 = {SCENT_AND_CARE_3};
	private static final double[] values44 = {1560};
	private static final String[] labels45 = {"Henkel AG & Co KGaA", "Beiersdorf AG"};
	private static final double[] values45 = {680, 880};
	private static final String[] labels46 = {AUTOMOBILE_1, RETAIL_1, RETAIL_3};
	private static final double[] values46 = {3317, 317, 1417};
	private static final String[] labels47 = {RETAIL_2};
	private static final double[] values47 = {317};
	private static final String[] labels48 = {"CECONOMY AG ST O.N."};
	private static final double[] values48 = {317};
	private static final String[] labels49 = {RETAIL_4};
	private static final double[] values49 = {1417};
	private static final String[] labels50 = {RETAIL_5};
	private static final double[] values50 = {1417};
	private static final String[] labels51 = {"Adidas AG"};
	private static final double[] values51 = {1417};
	private static final String[] labels52 = {AUTOMOBILE_2};
	private static final double[] values52 = {3317};
	private static final String[] labels53 = {AUTOMOBILE_3};
	private static final double[] values53 = {3317};
	private static final String[] labels54 = {"Daimler AG", "Volkswagen AG", "BMW AG"};
	private static final double[] values54 = {1042, 1131, 1143};
	private static final String[] labels55 = {TRANSPORT, INVESTMENT};
	private static final double[] values55 = {1567, 4264};
	private static final String[] labels56 = {AVIATION_3, AVIATION_1};
	private static final double[] values56 = {1318, 248};
	private static final String[] labels57 = {AVIATION_2};
	private static final double[] values57 = {248};
	private static final String[] labels58 = {"Deutsche Lufthansa AG"};
	private static final double[] values58 = {248};
	private static final String[] labels59 = {AVIATION_4};
	private static final double[] values59 = {1318};
	private static final String[] labels60 = {"Deutsche Post AG"};
	private static final double[] values60 = {1318};
	private static final String[] labels61 = {INDUSTRY_1, DEFENSE_2, INDUSTRY_3};
	private static final double[] values61 = {1587, 1374, 1302};
	private static final String[] labels62 = {INDUSTRY_2};
	private static final double[] values62 = {1587};
	private static final String[] labels63 = {"LINDE PLC EO 0,001"};
	private static final double[] values63 = {1587};
	private static final String[] labels64 = {INDUSTRY_4};
	private static final double[] values64 = {1302};
	private static final String[] labels65 = {"Siemens AG"};
	private static final double[] values65 = {1302};
	private static final String[] labels66 = {DEFENSE_1};
	private static final double[] values66 = {1374};
	private static final String[] labels67 = {"MTU AERO ENGINES NA O.N."};
	private static final double[] values67 = {1374};
	private static final String[] labels68 = {BUILDING_MATERIALS_1};
	private static final double[] values68 = {2132};
	private static final String[] labels69 = {BUILDING_MATERIALS_2, CHEMICALS_1};
	private static final double[] values69 = {726, 1406};
	private static final String[] labels70 = {BUILDING_MATERIALS_3};
	private static final double[] values70 = {726};
	private static final String[] labels71 = {"HeidelbergCement AG"};
	private static final double[] values71 = {726};
	private static final String[] labels72 = {CHEMICALS_2};
	private static final double[] values72 = {1406};
	private static final String[] labels73 = {"COVESTRO AG  O.N.", "BASF SE"};
	private static final double[] values73 = {651, 755};

	@Inject
	public ComplexPieChart(Composite parent) {

		super(parent, SWT.NONE);
		try {
			initialize();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() throws Exception {

		/*
		 * Create series.
		 */
		ICircularSeriesData multiLevelDoughnut = new CircularSeriesData();
		//
		multiLevelDoughnut.setTitle("DAX");
		multiLevelDoughnut.setNodeClass("Taxonomie");
		multiLevelDoughnut.setValueClass("Value (€)");
		multiLevelDoughnut.setSeries(labels1, values1);
		//
		multiLevelDoughnut.getNodeById(WITHOUT_CLASSIFICATION).addChildren(labels2, values2);
		multiLevelDoughnut.getNodeById(SECTOR_GICS).addChildren(labels3, values3);
		multiLevelDoughnut.getNodeById(PUBLIC_UTILITY_2).addChildren(labels4, values4);
		multiLevelDoughnut.getNodeById(PUBLIC_UTILITY_1).addChildren(labels5, values5);
		multiLevelDoughnut.getNodeById(PUBLIC_MULTI_UTILITY_2).addChildren(labels6, values6);
		multiLevelDoughnut.getNodeById(PUBLIC_MULTI_UTILITY_1).addChildren(labels7, values7);
		multiLevelDoughnut.getNodeById(PUBLIC_RELATIONS).addChildren(labels8, values8);
		multiLevelDoughnut.getNodeById(TELECOMMUNICATIONS).addChildren(labels9, values9);
		multiLevelDoughnut.getNodeById(TELECOMMUNICATIONS_DIVERSE).addChildren(labels10, values10);
		multiLevelDoughnut.getNodeById(TELECOMMUNICATIONS_INTEGRATED).addChildren(labels11, values11);
		multiLevelDoughnut.getNodeById(INFORMATION_TECHNOLOGY).addChildren(labels12, values12);
		multiLevelDoughnut.getNodeById(SEMICONDUCTOR_PRODUCTION_2).addChildren(labels13, values13);
		multiLevelDoughnut.getNodeById(SEMICONDUCTOR_PRODUCTION_1).addChildren(labels14, values14);
		multiLevelDoughnut.getNodeById(SEMICONDUCTOR).addChildren(labels15, values15);
		multiLevelDoughnut.getNodeById(SOFTWARE_SERVICES).addChildren(labels16, values16);
		multiLevelDoughnut.getNodeById(SOFTWARE).addChildren(labels17, values17);
		multiLevelDoughnut.getNodeById(SOFTWARE_APPLICATIONS).addChildren(labels18, values18);
		multiLevelDoughnut.getNodeById(FINANCIAL).addChildren(labels19, values19);
		multiLevelDoughnut.getNodeById(REAL_ESTATE).addChildren(labels20, values20);
		multiLevelDoughnut.getNodeById(REAL_ESTATE_BASIS).addChildren(labels21, values21);
		multiLevelDoughnut.getNodeById(REAL_ESTATE_ACTIVITIES).addChildren(labels22, values22);
		multiLevelDoughnut.getNodeById(INSURANCE_2).addChildren(labels23, values23);
		multiLevelDoughnut.getNodeById(INSURANCE_1).addChildren(labels24, values24);
		multiLevelDoughnut.getNodeById(REINSURANCE).addChildren(labels25, values25);
		multiLevelDoughnut.getNodeById(ACCIDENT_INSURANCE).addChildren(labels26, values26);
		multiLevelDoughnut.getNodeById(FINANCIAL_SERVICES_2).addChildren(labels27, values27);
		multiLevelDoughnut.getNodeById(CAPITAL_MARKET).addChildren(labels28, values28);
		multiLevelDoughnut.getNodeById(CUSTODIAN).addChildren(labels29, values29);
		multiLevelDoughnut.getNodeById(FINANCIAL_SERVICES_DIVERSE).addChildren(labels30, values30);
		multiLevelDoughnut.getNodeById(FINANCIAL_SERVICES_SPECIALIZED).addChildren(labels31, values31);
		multiLevelDoughnut.getNodeById(BANK_2).addChildren(labels32, values32);
		multiLevelDoughnut.getNodeById(BANK_1).addChildren(labels33, values33);
		multiLevelDoughnut.getNodeById(BANK_GENERAL).addChildren(labels34, values34);
		multiLevelDoughnut.getNodeById(HEALTHCARE).addChildren(labels35, values35);
		multiLevelDoughnut.getNodeById(PHARMA_1).addChildren(labels36, values36);
		multiLevelDoughnut.getNodeById(PHARMA_2).addChildren(labels37, values37);
		multiLevelDoughnut.getNodeById(PHARMA_3).addChildren(labels38, values38);
		multiLevelDoughnut.getNodeById(HEALTHCARE_SERVICES_1).addChildren(labels39, values39);
		multiLevelDoughnut.getNodeById(HEALTHCARE_SERVICES_2).addChildren(labels40, values40);
		multiLevelDoughnut.getNodeById(HEALTHCARE_SERVICES_3).addChildren(labels41, values41);
		multiLevelDoughnut.getNodeById(CUSTOMER_GOODS).addChildren(labels42, values42);
		multiLevelDoughnut.getNodeById(SCENT_AND_CARE_1).addChildren(labels43, values43);
		multiLevelDoughnut.getNodeById(SCENT_AND_CARE_2).addChildren(labels44, values44);
		multiLevelDoughnut.getNodeById(SCENT_AND_CARE_3).addChildren(labels45, values45);
		multiLevelDoughnut.getNodeById(LUXURY_GOODS).addChildren(labels46, values46);
		multiLevelDoughnut.getNodeById(RETAIL_1).addChildren(labels47, values47);
		multiLevelDoughnut.getNodeById(RETAIL_2).addChildren(labels48, values48);
		multiLevelDoughnut.getNodeById(RETAIL_3).addChildren(labels49, values49);
		multiLevelDoughnut.getNodeById(RETAIL_4).addChildren(labels50, values50);
		multiLevelDoughnut.getNodeById(RETAIL_5).addChildren(labels51, values51);
		multiLevelDoughnut.getNodeById(AUTOMOBILE_1).addChildren(labels52, values52);
		multiLevelDoughnut.getNodeById(AUTOMOBILE_2).addChildren(labels53, values53);
		multiLevelDoughnut.getNodeById(AUTOMOBILE_3).addChildren(labels54, values54);
		multiLevelDoughnut.getNodeById(INDUSTRY).addChildren(labels55, values55);
		multiLevelDoughnut.getNodeById(TRANSPORT).addChildren(labels56, values56);
		multiLevelDoughnut.getNodeById(AVIATION_1).addChildren(labels57, values57);
		multiLevelDoughnut.getNodeById(AVIATION_2).addChildren(labels58, values58);
		multiLevelDoughnut.getNodeById(AVIATION_3).addChildren(labels59, values59);
		multiLevelDoughnut.getNodeById(AVIATION_4).addChildren(labels60, values60);
		multiLevelDoughnut.getNodeById(INVESTMENT).addChildren(labels61, values61);
		multiLevelDoughnut.getNodeById(INDUSTRY_1).addChildren(labels62, values62);
		multiLevelDoughnut.getNodeById(INDUSTRY_2).addChildren(labels63, values63);
		multiLevelDoughnut.getNodeById(INDUSTRY_3).addChildren(labels64, values64);
		multiLevelDoughnut.getNodeById(INDUSTRY_4).addChildren(labels65, values65);
		multiLevelDoughnut.getNodeById(DEFENSE_2).addChildren(labels66, values66);
		multiLevelDoughnut.getNodeById(DEFENSE_1).addChildren(labels67, values67);
		multiLevelDoughnut.getNodeById(BASIC_GOODS).addChildren(labels68, values68);
		multiLevelDoughnut.getNodeById(BUILDING_MATERIALS_1).addChildren(labels69, values69);
		multiLevelDoughnut.getNodeById(BUILDING_MATERIALS_2).addChildren(labels70, values70);
		multiLevelDoughnut.getNodeById(BUILDING_MATERIALS_3).addChildren(labels71, values71);
		multiLevelDoughnut.getNodeById(CHEMICALS_1).addChildren(labels72, values72);
		multiLevelDoughnut.getNodeById(CHEMICALS_2).addChildren(labels73, values73);
		//
		ICircularSeriesSettings settings = multiLevelDoughnut.getSettings();
		settings.setDescription("DAX Taxonomie");
		settings.setBorderStyle(LineStyle.SOLID);
		//
		multiLevelDoughnut.getSettings().setSeriesType(SeriesType.DOUGHNUT);
		/*
		 * Set series.
		 * ICircularSeriesData
		 */
		addSeriesData(multiLevelDoughnut);
	}
}