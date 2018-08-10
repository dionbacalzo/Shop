<div id="uploadItemtitle" class="col-12">
	Item List
</div>
<table id= "uploadItemTable" class="col-12">
	<thead>
		<th>Title</th>
		<th>Price</th>
		<th>Type</th>
		<th>Manufacturer</th>
		<th>Release Date</th>
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
	</tr>
	{{/itemList}}
	</tbody>
</table>