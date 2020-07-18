function addStyles (win, styles) {
  let mainHead = window.document.getElementsByTagName('head')[0].innerHTML;
  win.document.getElementsByTagName('head')[0].innerHTML = mainHead;
  styles.forEach(style => {
    let link = win.document.createElement('link');
    link.setAttribute('rel', 'stylesheet');
    link.setAttribute('type', 'text/css');
    link.setAttribute('href', style);
    win.document.getElementsByTagName('head')[0].appendChild(link);
  });
}

const PrintPage = {
  install (Vue, options = {}) {
    Vue.prototype.$htmlToPaper = (el, localOptions, cb = () => true) => {
      let defaultName = '_blank', 
          defaultSpecs = ['fullscreen=no','titlebar=no', 'scrollbars=no'],
          defaultReplace = true,
          defaultStyles = []
      let {
        name = defaultName,
        specs = defaultSpecs,
        replace = defaultReplace,
        styles = defaultStyles
      } = options;

      // If has localOptions
      // TODO: improve logic
      if (localOptions) {
        if (localOptions.name) name = localOptions.name;
        if (localOptions.specs) specs = localOptions.specs;
        if (localOptions.replace) replace = localOptions.replace;
        if (localOptions.styles) styles = localOptions.styles;
      }

      specs = specs.length ? specs.join(',') : '';

      const element = window.document.getElementById(el);
      let template = "";
      if (!element) {
        template = el;
      } else {
        template = element.innerHTML;
      }
      
      const url = '';
      const win = window.open(url, name, specs, replace,);

      win.document.write(`
        <html>
          <head>
            <title>未収金処理システム${window.document.title}</title>
          </head>
          <body>
            <style type="text/css" media="all">
              body {
                width: auto;
              }
              #print-content {
                -webkit-print-color-adjust: exact;
              }
              #print-content .request {
                height: auto;
                max-height: unset;
              }
              #print-content .request-header{
                width: 100%;
              }
              #print-content .request-header .title{
                max-width: unset;
              }
              #print-content .request-body {
                max-height: unset;
                height: auto;
                width: auto;
                overflow: visible;
              }
              #print-content .table-container > div {
                max-width: unset;
                width: auto;
              }
              #print-content .table-container .table {
                width: auto;
                max-width: unset;
              }
              #print-content .table-container .table th{
                color: #000;
              }
              #print-content .approval-flow,
              #print-content .request-header_status,
              #print-content .request-footer.btnForRequest,
              #print-content .flow-history,
              #print-content svg {
                display: none !important;
              }
              #print-content input {
                border: none !important;
              }
              #print-content .customScrollBar {
                width: auto;
              }
              #print-content .table_payment {
                font-size: 1rem;
                border-spacing: 0.1rem;
                border-collapse: separate;
              }
              #print-content .table_payment td {
                border: 1px solid #fff !important;
              }
              #print-content .form-group.label_input{
                background-color: grey;
                margin: 0;
                font-size: .75rem;
                display: flex;
                flex-direction: row;
              }
              #print-content input {
                border: 1px solid #00000099 !important;
                background: #fff !important;
                padding: 6px 8px;
                padding-left: 15px;
              }
              #print-content .form-group.label_input input {
                border-left: none !important;
              }
              #print-content .table_payment .form-group.label_input  input{
                border: 1px solid #00000099 !important;
                background: #fff !important;
              }
              span.fieldDependent {
                width: 100%;
              }
              .col-sm-8 {
                flex: 0 0 65%;
                max-width: 65%;
              }
              .col-sm-4 {
                flex: 0 0 35%;
                max-width: 35%;
              }
              .col-sm-4.other-purpose {
                flex: 0 0 30%;
                max-width: 30%;
              }
              #print-content .mt-3 {
                  margin-top: 0.8rem !important;
              }
              @page {
                size: landscape;
                margin: 0 15mm;
              }
              @media print {
              }
              @media print {
                @page {
                  size: landscape;
                }
              }
            </style>
            <div id="print-content">
              ${template}
            </div>
          </body>
        </html>
      `);

      addStyles(win, styles);
      
      setTimeout(() => {
        win.document.close();
        win.focus();
        win.print();
        win.close();
        cb();
      }, 1000);
      return true;
    };
  }
}

export default PrintPage;