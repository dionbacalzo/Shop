
 
$.ajax({
	url :  contextPath + "/accountResetList",
	type : "POST",
	beforeSend : function() {
		//$loader.show();
	},
	complete : function() {
		//$loader.hide();
	},
	success : function(data) {		
		createAccountList(JSON.parse(data));
	},
	error : function(data) {
		
	}
});

function createAccountList(users){	
	if (users && users.users !== undefined) {
		usersList = users.users;
		var container = $('#accountListContainer');
		container.empty();
		
		if (usersList.length && usersList.length > 0) {
			
			var $form = $('<form />', { id: 'resetForm', action: contextPath+"/resetAccount", class: 'col-12' });
			$form.appendTo(container);
			usersList.forEach(function (user) {
				$('<input />', { type: 'checkbox', id: 'id-'+user.username, name: user.username, class: 'col-2' }).appendTo($form);
				$('<label />', { 'for': 'id-'+user.username, text: user.username, class: 'col-10' }).appendTo($form);
			});
			var $submitdiv = $('<div />', { id: 'submitContainer', class: 'col-12' });
			$('<input />', { type:'submit', value:'Submit' }).appendTo($submitdiv);
			$submitdiv.appendTo($form);
			initializeResetForm($form)
		} else {
			container.text("No Accounts to display");
		}
	}
}

function initializeResetForm($resetForm){
	$resetForm.submit(function(event) {
		event.preventDefault();
		
		var userList = []
		var formlist = $resetForm.serializeArray();
		formlist.forEach((user) => {
			newUser = {
				'username' : user.name	
			}
			
			userList.push(newUser);
		})
		
		$.ajax({
			url :  contextPath + "/resetAccount",
			type : "POST",
			data : JSON.stringify(userList),
			contentType : "application/json; charset=utf-8",
			beforeSend : function() {
				//$loader.show();
			},
			complete : function() {
				//$loader.hide();
			},
			success : function(data) {
				console.log(data);
				createAccountList(JSON.parse(data));
			},
			error : function(data) {
				
			}
		});
		
	});
}