<html>
<body>
  <style>.matched{background-color:#CC99FF;}</style>
  <#list groups as group>
    <hr/>
    <h3>Length: ${group.matchedLength} Mutations: ${group.mutationsCount}</h3>
    <#list group.matches as match>
      <div>
        <b>${match.name}</b>:&nbsp;${match.htmlSequence}
      </div>
    </#list>
    <br/>
    <br/>
  </#list>
</body>
</html>