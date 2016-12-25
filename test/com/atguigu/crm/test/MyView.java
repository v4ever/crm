package com.atguigu.crm.test;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

@Component 
public class MyView extends AbstractView{

	@Override
	protected void renderMergedOutputModel(Map<String, Object> map,
			HttpServletRequest arg1, HttpServletResponse arg2) throws Exception {
		PieDataset paramPieDataset = createDataset(map);
		JFreeChart chart = createChart(paramPieDataset);
		
		//直接把结果输出到输出流中
		ChartUtilities.writeChartAsJPEG(arg2.getOutputStream(), chart, 500, 270);
	}
	
	private PieDataset createDataset(Map<String, Object> map) {
		DefaultPieDataset localDefaultPieDataset = new DefaultPieDataset();
		/*
		localDefaultPieDataset
				.setValue("Java", new Double(43.200000000000003D));
		localDefaultPieDataset.setValue("Visual Basic", new Double(10.0D));
		localDefaultPieDataset.setValue("C/C++", new Double(17.5D));
		localDefaultPieDataset.setValue("PHP", new Double(32.5D));
		localDefaultPieDataset.setValue("Perl", null);
		*/
		
		for(Map.Entry<String, Object> entry: map.entrySet()){
			localDefaultPieDataset.setValue(entry.getKey(), 
					(int)entry.getValue());
		}
		
		return localDefaultPieDataset;
	}

	private JFreeChart createChart(PieDataset paramPieDataset) {
		JFreeChart localJFreeChart = ChartFactory.createPieChart3D(
				"Pie Chart 3D Demo 1", paramPieDataset, true, true, false);
		PiePlot3D localPiePlot3D = (PiePlot3D) localJFreeChart.getPlot();
		localPiePlot3D.setDarkerSides(true);
		localPiePlot3D.setStartAngle(290.0D);
		localPiePlot3D.setDirection(Rotation.CLOCKWISE);
		localPiePlot3D.setForegroundAlpha(0.5F);
		localPiePlot3D.setNoDataMessage("No data to display");
		return localJFreeChart;
	}

}
