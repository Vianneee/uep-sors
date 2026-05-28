const fs = require('fs');
const files = ['apphistory.html','appli.html','cmo.html','register.html','regu.html','upao.html'];
files.forEach(f => {
  let c = fs.readFileSync(f, 'utf8');
  c = c.replace('</body>', '<script src="sors.js"></script>\n</body>');
  fs.writeFileSync(f, c);
  console.log('Updated ' + f);
});
console.log('Done!');
