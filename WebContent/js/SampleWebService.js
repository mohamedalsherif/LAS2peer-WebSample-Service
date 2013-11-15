	//Private Porperties
				var LAS2PEERHOST = "http://localhost:8080/";
		var LAS2PEERSERVICENAME = "i5.las2peer.services.webSampleService.WebSampleService";
		var LAS2peerClient; 
		/**
		* Handles submission of the Login Form. Tries to login to LAS2peer and shows the Methods selection on success.
		*/
		var login_form_submit = function()
		{
			var username = (document.getElementById("cs_username")).value,
				password = (document.getElementById("cs_password")).value;
			if(username != "" && password!="")
			{
				if(LAS2peerClient.getStatus() != "loggedIn")
				{
					LAS2peerClient.login(username, password, LAS2PEERHOST, "WebSampleServiceFrontend");
				}
			}
		};
		function loggedInSuccessfully()
		{
			document.getElementById("cs_loginform").style.visibility="hidden";
			document.getElementById("cs_logout").style.visibility="visible";
			document.getElementById("selectMethod").style.visibility="visible";
			LAS2peerClient.invoke(LAS2PEERSERVICENAME, "getMethods", "", function(status, result) 
			{
				if(status == 200 || status == 204) 
				{
					populateComboBox(result.value);
				} 
				else 
				{
					console.log("Error! Message: " + result);
				}
			});
			AddTextBoxes([]);
		}
		
		
		function populateComboBox(methodNamesArray)
		{
			var select = document.getElementById("selectMethod");
			select.innerHTML="";
			for(var i = 0; i < methodNamesArray.length; i++) 
			{
			    var opt = methodNamesArray[i];
			    var el = document.createElement("option");
			    el.textContent = opt;
			    el.value = opt;
			    select.appendChild(el);
			}
		}
		
		function logout()
		{
			LAS2peerClient.logout();
			document.getElementById("cs_loginform").style.visibility="visible";
			document.getElementById("cs_logout").style.visibility="hidden";
			document.getElementById("selectMethod").style.visibility="hidden";
			var div = document.getElementById("textBoxesDiv");
			div.innerHTML = '';
		}
		
		function ComboBoxChange()
		{
		    var e = document.getElementById("selectMethod");//get the combobox
		    var selMethod = e.options[e.selectedIndex].value;//get selected value
			invocationArguments = [ {
				"type" : "String",
				"value" : selMethod
			}];
		    LAS2peerClient.invoke(LAS2PEERSERVICENAME, "getParameterTypesOfMethod", invocationArguments, function(status, result) 
		    {
				if(status == 200 || status == 204) 
				{	
					AddTextBoxes(result.value);
				} 
				else 
				{
					console.log("Error! Message: " + result);
				}
			});                 
		}
		
		
		function InvokeButtonClicked()
		{
			var textBoxes=document.getElementsByClassName("argumentTextBoxes");
			var invocationArguments = [];
			for(var i=0;i<textBoxes.length;i++)
				{
					var elem =document.getElementById("argumentTextBoxes"+i);
					var val =elem.value;
					var parameterI={};
					parameterI.type = elem.placeholder;
					parameterI.value = val;
					invocationArguments.push(parameterI);
				}
			
			   var e = document.getElementById("selectMethod");//get the combobox
			    var selMethod = e.options[e.selectedIndex].value;//get selected value
			  LAS2peerClient.invoke(LAS2PEERSERVICENAME, selMethod, invocationArguments, function(status, result) 
			{
					if(status == 200 || status == 204) 
					{
						console.log("Message Invoked successfully");
						ShowResult(result);
					} 
					else 
					{
						console.log("Error! Message: " + result);
						var resultCorrectFormat={};
						resultCorrectFormat.value = result;
						ShowResult(resultCorrectFormat);
					}
				}); 
		}
		
		
		function ShowResult(result)
		{
			var text = document.createElement('p');
			if(result.type=="none")
			{
				text.innerHTML = "(void) Method";
			}
			else
			{
				text.innerHTML = result.value;
			}

			var div = document.getElementById("textBoxesDiv");
			div.appendChild(text);
		}
		
		function AddTextBoxes(parameters)
		{
			var div = document.getElementById("textBoxesDiv");
			div.innerHTML = '';
			for(var i = 0; i < parameters.length; i++) 
			{
				var input = document.createElement('input'); 
				input.type = "text"; 
				input.id="argumentTextBoxes"+i;
				input.className="argumentTextBoxes";
				input.placeholder=parameters[i];
				div.appendChild(input);
			}
			
			var button = document.createElement('input');
			button.type = "button"; 
			button.value="invoke Method";
			button.onclick=InvokeButtonClicked;
			div.appendChild(button);
		}
		
		window.onload = function() 
		{
			document.getElementById("cs_logout").style.visibility="hidden";
			document.getElementById("selectMethod").style.visibility="hidden";
			LAS2peerClient = new LasAjaxClient("WebSampleService", function(statusCode, message) 
					{
						switch(statusCode) 
							{
								case Enums.Feedback.LoginSuccess:
									console.log("Login successful!");
									loggedInSuccessfully();
									break;
								case Enums.Feedback.LogoutSuccess:
									console.log("Logout successful!");
									break;
								case Enums.Feedback.LoginError:
								case Enums.Feedback.LogoutError:
									console.log("Login error: " + statusCode + ", " + message);
									break;
								case Enums.Feedback.InvocationWorking:
								case Enums.Feedback.InvocationSuccess:
								case Enums.Feedback.Warning:
								case Enums.Feedback.PingSuccess:
									break;
								default:
									console.log("Unhandled Error: " + statusCode + ", " + message);
								break;
							}
					});
		};
		