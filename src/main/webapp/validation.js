function inputChange(){
		let text = document.querySelector('#searchNameText').value;
//		console.log(text);
		if(text){
			document.querySelector('#searchNameBtn').disabled = false;
		}else{
			document.querySelector('#searchNameBtn').disabled = true;
		}
}