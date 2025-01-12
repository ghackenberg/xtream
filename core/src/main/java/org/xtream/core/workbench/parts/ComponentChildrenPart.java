package org.xtream.core.workbench.parts;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.xtream.core.model.Component;
import org.xtream.core.model.Container;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.events.SelectionEvent;
import org.xtream.core.workbench.models.ElementTableModel;

public class ComponentChildrenPart<T extends Component> extends Part<T>
{
	
	private JTable table;

	public ComponentChildrenPart()
	{
		this(0, 0);
	}
	public ComponentChildrenPart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public ComponentChildrenPart(int x, int y, int width, int height)
	{
		super("Component children", ComponentChildrenPart.class.getClassLoader().getResource("parts/component_children.png"), x, y, width, height);
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		
		getPanel().add(new JScrollPane(table));
	}
	
	@Override
	public void handle(Event<T> event)
	{
		if (event instanceof SelectionEvent)
		{
			SelectionEvent<T> selection = (SelectionEvent<T>) event;
			
			Container current = selection.getElementByClass(Container.class);
			
			table.setModel(new ElementTableModel(current));
			
			for (int column = 0; column < 3; column++)
			{
				int width = 0;
				
				for (int row = 0; row < table.getRowCount(); row++)
				{
					TableCellRenderer renderer = table.getCellRenderer(row, column);
					
					width = Math.max(table.prepareRenderer(renderer, row, column).getPreferredSize().width, width);
				}
				
				table.getColumnModel().getColumn(column).setPreferredWidth(width);
			}
		}
	}

}
