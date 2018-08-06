package pl.ankiety.pomocniczeKlasy;


public class Paging{
	private int pollsOnSize;
	private int pollsOffSize;
	
	public Paging(int pollsOnSize, int pollsOffSize){
		this.pollsOnSize = pollsOnSize;
		this.pollsOffSize = pollsOffSize;
	}
	
	public StringBuilder pagesOn(int currentPage){
		StringBuilder pages = new StringBuilder("");
		
		if(pollsOnSize > 10){
			pages.append("<br> <FONT style=\"font-size:2vmin;\">[strona]</FONT>");
			
			int iPage = 1;
			int lastPage = (pollsOnSize + 9) / 10;
			
			pages.append(pageOn(currentPage, iPage));
			
			if(currentPage >= 1 + 3)
				pages.append(" ...");
			
			while(++iPage <= currentPage + 1 && iPage < lastPage)
				if(iPage >= currentPage - 1)
					pages.append(pageOn(currentPage, iPage));
			
			if(currentPage <= lastPage - 3)
				pages.append(" ...");
			
			pages.append(pageOn(currentPage, lastPage));
			HTML h = new HTML();
			pages.append(" <INPUT ID=\"goToPageOn\" style=\"width:5vw; height: 2vh; font-size:2vmin;\" type=\"text\" placeholder=\"strona\">");
			pages.append(" <INPUT ID=\"goToPageOnButton\" TYPE=\"submit\" style=\""+ h.smallButton2()+"margin: 2vh 0 0 0; \""
                                + "VALUE=\"Przejdź do wybranej\" onClick=\"setPageOn(document.forms['formularz1'].elements['goToPageOn'].value)\">");
		}
		
		return pages;
	}
	private String pageOn(int currentPage, int p){
		if(currentPage == p)
			return "<font style=\"font-size:2vmin;\"> " + p + "</font>";
		return " <INPUT style=\"padding:0 0.1vw 0 0; font-size:2vmin; width:1.5vw; height: 3vh;\" ID=\"pageOn" + p + "\" TYPE=\"submit\" VALUE=\"" + p + "\" onClick=\"setPageOn(" + p + ")\">";
	}
	public StringBuilder pagesOff(int currentPage){
		StringBuilder pages = new StringBuilder("");
		
		if(pollsOffSize > 10){
			pages.append("<br> <FONT style=\"font-size:2vmin;\">[strona]</FONT>");
			
			int iPage = 1;
			int lastPage = (pollsOffSize + 9) / 10;
			
			pages.append(pageOff(currentPage, iPage));
			
			if(currentPage >= 1 + 3)
				pages.append(" ...");
			
			while(++iPage <= currentPage + 1 && iPage < lastPage)
				if(iPage >= currentPage - 1)
					pages.append(pageOff(currentPage, iPage));
			
			if(currentPage <= lastPage - 3)
				pages.append(" ...");
			
			pages.append(pageOff(currentPage, lastPage));
			HTML h = new HTML();
			pages.append(" <INPUT ID=\"goToPageOff\" style=\"width:5vw; height: 2vh; font-size:2vmin; \" type=\"text\" placeholder=\"strona\">");
			pages.append(" <INPUT ID=\"goToPageOffButton\" TYPE=\"submit\"style=\""+ h.smallButton2()+"margin: 2vh 0 0 0;\""
                                + "VALUE=\"Przejdź do wybranej\" onClick=\"setPageOff(document.forms['formularz1'].elements['goToPageOff'].value)\">");
		}
		
		return pages;
	}
	private String pageOff(int currentPage, int p){
		if(currentPage == p)
			return "<font style=\"font-size:2vmin;\"> " + p + "</font>";
		return " <INPUT style=\"padding:0 0.1vw 0 0; font-size:2vmin; width:1.5vw; height: 3vh;\" ID=\"pageOff" + p + "\" TYPE=\"submit\" VALUE=\"" + p + "\" onClick=\"setPageOff(" + p + ")\">";
	}
}
