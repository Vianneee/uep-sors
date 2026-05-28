const fs = require('fs');

// Fix event pages - change react to reactGated
const eventFiles = {
  'event1.html': 'event1',
  'event22.html': 'event22',
  'event.html': 'event3'
};

for (const [filename, articleId] of Object.entries(eventFiles)) {
  let content = fs.readFileSync(filename, 'utf8');
  content = content.replaceAll(`onclick="react('${articleId}'`, `onclick="reactGated('${articleId}'`);
  content = content.replaceAll(`[onclick="react('${articleId}'`, `[onclick="reactGated('${articleId}'`);
  content = content.replace('</body>', '<script src="sors.js"></script>\n</body>');
  fs.writeFileSync(filename, content);
  console.log('Updated ' + filename);
}

// Add sors.js to teammate and other files
const simpleFiles = ['org.html','orgdetails.html','adminreg.html','regu.html','upao.html','cmo.html','appli.html'];
for (const filename of simpleFiles) {
  let content = fs.readFileSync(filename, 'utf8');
  content = content.replace('</body>', '<script src="sors.js"></script>\n</body>');
  fs.writeFileSync(filename, content);
  console.log('Updated ' + filename);
}

// Add sors.js + guest gate to apphistory and register
const gatedFiles = {
  'apphistory.html': 'Please sign in to view your application history.',
  'register.html': 'Please sign in to apply to an organization.'
};
for (const [filename, msg] of Object.entries(gatedFiles)) {
  let content = fs.readFileSync(filename, 'utf8');
  content = content.replace('</body>', `<script src="sors.js"></script>\n<script>requireLogin("${msg}");</script>\n</body>`);
  fs.writeFileSync(filename, content);
  console.log('Updated ' + filename);
}

console.log('All done!');