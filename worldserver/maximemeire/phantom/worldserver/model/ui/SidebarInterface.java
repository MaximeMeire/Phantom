package maximemeire.phantom.worldserver.model.ui;

public class SidebarInterface {
	
	public final int icon;
	public final int interfaceId;
	
	public SidebarInterface(int icon, int interfaceId) {
		this.icon = icon;
		this.interfaceId = interfaceId;
	}
	
	public static final SidebarInterface[] STANDARD_SIDEBAR = new SidebarInterface[] {
		new SidebarInterface(0, 2422),
		new SidebarInterface(1, 3917),
		new SidebarInterface(2, 638),
		new SidebarInterface(3, 3213),
		new SidebarInterface(4, 1644),
		new SidebarInterface(5, 5608),
		new SidebarInterface(6, 1151),
		new SidebarInterface(8, 5065),
		new SidebarInterface(9, 5715),
		new SidebarInterface(10, 2449),
		new SidebarInterface(11, 4445),
		new SidebarInterface(12, 147),
		new SidebarInterface(13, 6299)
	};

}
