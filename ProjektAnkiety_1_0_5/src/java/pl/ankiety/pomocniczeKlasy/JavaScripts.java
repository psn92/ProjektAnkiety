package pl.ankiety.pomocniczeKlasy;

public class JavaScripts {
	public JavaScripts(){
	}
	
	
	public String setSleepFunction(){
		return "<SCRIPT>function sleep(milliseconds){"
				+ "	var start = new Date().getTime();"
				+ "	for(var k = 0; k < 1e7; k++)"
				+ "		if((new Date().getTime() - start) > milliseconds)"
				+ "			break;"
				+ "}</SCRIPT>";
	}
	
	private StringBuilder PollsWizardPoolsValidation_Title(){
		StringBuilder validationScript = new StringBuilder(" function poolsValidation_Title(){");
		validationScript.append("	var result = true;");
		
		validationScript.append("	if(!/\\S+/.test(document.forms['formularz1'].elements['title'].value)){");
		validationScript.append("		document.getElementById('title').focus();");
		validationScript.append("		sleep(100);");
		validationScript.append("		alert('Uzupełnij tytył ankiety.');");
		validationScript.append("		result = false;");
		validationScript.append("	}");
		
		validationScript.append("	return result;");
		validationScript.append("}");
		
		
		return validationScript;
	}
	private StringBuilder PollsWizardPoolsValidation_pools_n(){
		StringBuilder validationScript = new StringBuilder(" function poolsValidation_pools_n(result){");
		validationScript.append("	if(!/\\S+/.test(document.forms['formularz1'].elements['pools_n'].value)){");
		validationScript.append("		document.getElementById('pools_n').focus();");
		validationScript.append("		sleep(100);");
		validationScript.append("		alert('Uzupełnij wymaganą ilość ankiet.');");
		validationScript.append("		result = false;");
		validationScript.append("	}");
		
		validationScript.append("	return result;");
		validationScript.append("}");
		
		
		return validationScript;
	}
	private StringBuilder PollsWizardPoolsValidation_questions(){
		StringBuilder validationScript = new StringBuilder(" function poolsValidation_questions(result){");
		validationScript.append("	var questionExists = false;");
		validationScript.append("	var unfilledQUESTION = 0;");
		validationScript.append("	var unfilledOPTION = 0;");
		validationScript.append("	for(var i = 0; i < document.forms['formularz1'].elements['n'].value; i++)");
		validationScript.append("		if(document.forms['formularz1'].elements['widocznePytanie' + i].value != 'false'){");
		validationScript.append("			questionExists = true;");
		validationScript.append("			if(!/\\S+/.test(document.forms['formularz1'].elements['question' + i].value)){");
		validationScript.append("				document.getElementById('question' + i).focus();");
		validationScript.append("				sleep(100);");
		validationScript.append("				alert('Brakuje treści pytania [niewypełnione pytanie nr ' + ++unfilledQUESTION + '].');");
		validationScript.append("				result = false;");
		validationScript.append("			}");
		validationScript.append("			result = poolsValidation_options(unfilledOPTION, i, result);");
		validationScript.append("		}");
		
		validationScript.append("		if(!questionExists)");
		validationScript.append("			alert('W ankiecie musi być minimum jedno pytanie.');");
		
		validationScript.append("	return result && questionExists;");
		validationScript.append("}");
		
		
		return validationScript;
	}
	private StringBuilder PollsWizardPoolsValidation_options(){
		StringBuilder validationScript = new StringBuilder(" function poolsValidation_options(unfilledOPTION, i, result){");
		validationScript.append("	var optionsExists = 0;");
		validationScript.append("	for(var j = 0; j < document.forms['formularz1'].elements['n' + i].value; j++){");
		validationScript.append("		var tmp = 'option' + i + '_' + j;");
		validationScript.append("		if(document.forms['formularz1'].elements['widocznaOpcja' + tmp].value != 'false'){");
		validationScript.append("			++optionsExists;");
		validationScript.append("			if(!/\\S+/.test(document.forms['formularz1'].elements[tmp].value)){");
		validationScript.append("				document.getElementById(tmp).focus();");
		validationScript.append("				sleep(100);");
		validationScript.append("				alert('Nie została wypełniona treść opcji [niewypełniona opcja nr ' + ++unfilledOPTION + '].');");
		validationScript.append("				result = false;");
		validationScript.append("			}");
		validationScript.append("		}");
		validationScript.append("	}");
		
		validationScript.append("	if(optionsExists < 2){");
		validationScript.append("		result = false;");
		validationScript.append("		document.getElementById('question' + i).focus();");
		validationScript.append("		sleep(100);");
		validationScript.append("		alert('Każde pytanie musi mieć co najmniej dwie możliwe odpowiedzi.\\n"
				+ "Dotyczy zaznaczonego w tle pytania.');");
		validationScript.append("	}");
		
		validationScript.append("	return result;");
		validationScript.append("}");
		
		
		return validationScript;
	}
	public StringBuilder PollsWizardPoolsValidation(){
		StringBuilder validationScript = new StringBuilder(" function poolsValidation(){");
		validationScript.append("	var result = poolsValidation_Title();");
		validationScript.append("	result = poolsValidation_pools_n(result);");
		validationScript.append("	result = poolsValidation_questions(result);");
		
		validationScript.append("	return result;");
		validationScript.append("} ");
		
		validationScript.append(PollsWizardPoolsValidation_Title());
		validationScript.append(PollsWizardPoolsValidation_pools_n());
		validationScript.append(PollsWizardPoolsValidation_questions());
		validationScript.append(PollsWizardPoolsValidation_options());
		
		
		return validationScript;
	}
	public StringBuilder PollsWizardPoolsNumberValidation(){
		StringBuilder validationScript = new StringBuilder(" function poolsNumberValidation(){");
		validationScript.append("	if(/^$/.test(document.forms['formularz1'].elements['pools_n'].value)");
		validationScript.append(" || /^[1-9]\\d*$/.test(document.forms['formularz1'].elements['pools_n'].value))");
		validationScript.append("		return ;");
		
		validationScript.append("	var errorComand = '';");
		validationScript.append("	var onlyNumbers = false;");
		
		validationScript.append("	if(/^\\D+$/.test(document.forms['formularz1'].elements['pools_n'].value)){");
		validationScript.append("		document.forms['formularz1'].elements['pools_n'].value = '0';");
		validationScript.append("		if(!onlyNumbers){");
		validationScript.append("			errorComand += 'Wymagana liczba ankiet musi zawierać tylko cyfry.\\n';");
		validationScript.append("			onlyNumbers = true;");
		validationScript.append("		}");
		validationScript.append("	}");
		
		
		validationScript.append("	if(/\\D+(\\d.*$)/.test(document.forms['formularz1'].elements['pools_n'].value)){");
		validationScript.append("		document.forms['formularz1'].elements['pools_n'].value = "
				+ "document.forms['formularz1'].elements['pools_n'].value.replace(/\\D+(\\d.*$)/, \"$1\");");
		validationScript.append("		if(!onlyNumbers){");
		validationScript.append("			errorComand += 'Wymagana liczba ankiet musi zawierać tylko cyfry.\\n';");
		validationScript.append("			onlyNumbers = true;");
		validationScript.append("		}");
		validationScript.append("	}");
		
		validationScript.append("	while(/(^\\d+)\\D+(\\d.*$)/.test(document.forms['formularz1'].elements['pools_n'].value)){");
		validationScript.append("		document.forms['formularz1'].elements['pools_n'].value = "
				+ "document.forms['formularz1'].elements['pools_n'].value.replace(/(^\\d+)\\D+(\\d.*$)/, \"$1$2\");");
		validationScript.append("		if(!onlyNumbers){");
		validationScript.append("			errorComand += 'Wymagana liczba ankiet musi zawierać tylko cyfry.\\n';");
		validationScript.append("			onlyNumbers = true;");
		validationScript.append("		}");
		validationScript.append("	}");
		
		validationScript.append("	if(/(^\\d+)\\D+$/.test(document.forms['formularz1'].elements['pools_n'].value)){");
		validationScript.append("		document.forms['formularz1'].elements['pools_n'].value = "
				+ "document.forms['formularz1'].elements['pools_n'].value.replace(/(^\\d+)\\D+$/, \"$1\");");
		validationScript.append("		if(!onlyNumbers){");
		validationScript.append("			errorComand += 'Wymagana liczba ankiet musi zawierać tylko cyfry.\\n';");
		validationScript.append("			onlyNumbers = true;");
		validationScript.append("		}");
		validationScript.append("	}");
		
		validationScript.append("	document.forms['formularz1'].elements['pools_n'].value = "
				+ "parseInt(document.forms['formularz1'].elements['pools_n'].value);");
		
		validationScript.append("	if(/^0$/.test(document.forms['formularz1'].elements['pools_n'].value)){");
		validationScript.append("		document.forms['formularz1'].elements['pools_n'].value = '1';");
		validationScript.append("		errorComand += 'Minimalna liczba ankiet to 1.\\n'");
		validationScript.append("	}");
		
		validationScript.append("	document.getElementById('pools_n').focus();");
		validationScript.append("	alert(errorComand + 'Dokonana została korekta zawartości pola.');");
		validationScript.append("} ");
		
		return validationScript;
	}
}
