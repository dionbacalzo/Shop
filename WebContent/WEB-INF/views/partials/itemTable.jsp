<div  class="col-12">
	<button id="addItem">Add</button>
</div>
<div id="uploadItemtitle" class="col-12">
	Item List
</div>
<table id="uploadItemTable" class="col-12">
	<thead>
		<th>Title</th>
		<th>Price</th>
		<th>Type</th>
		<th>Manufacturer</th>
		<th>Release Date</th>
		<th>Delete</th>
	</thead>
	<tbody>
	{{#itemList}}
	<tr>
		<td>
			{{title}}
		</td>
		<td>
			{{price}}
		</td>
		<td>
			{{type}}
		</td>
		<td>
			{{manufacturer}}
		</td>
		<td>
			{{releaseDate}}
		</td>
		<td>
			<button class="deleteButton"></button>
		</td>
		<td class="hidden">
			<form class="hidden" method="POST" action="${pageContext.request.contextPath}">
				<input class="hidden" name="id" type="text" value="{{id}}"/>
				<input class="hidden" name="title" type="text" value="{{title}}"/>
				<input class="hidden" name="price" type="text" value="{{price}}"/>
				<input class="hidden" name="type" type="text" value="{{type}}"/>
				<input class="hidden" name="manufacturer" type="text" value="{{manufacturer}}"/>
				<input class="hidden" name="releaseDate" type="text" value="{{releaseDate}}"/>
			</form>
		</td>
	</tr>
	{{/itemList}}
	</tbody>
</table>