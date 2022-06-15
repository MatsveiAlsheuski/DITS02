
const resultTableBody = document.getElementById('resultTableBody');
const dataContainer = document.getElementById('dataContainer');
const sortTestsButton = document.getElementById('sortTestsButton');
let isReverseTest = false;
const baseUrl = window.location.origin;

function updateResult(data) {
    if (!data) {
        return
    }

    // TODO
    // remove all debug output
    console.log(isReverseTest)

    // TODO:
    // move this string below to separate function
    const reversedData = isReverseTest ? data.reverse() : data;
    dataContainer.classList.add('active');
    resultTableBody.innerHTML = `
    ${reversedData.map(({testName, count, avgProc, questionStatistics}, index) => {
        return `
      <div class="container test" data-id="${index}">
        <div class="row result-row" data-bs-toggle="collapse" href='#test${index}'>
          <div class="col-1 text-start d-none d-md-block">${index + 1}</div>
          <div class="col-md-8 col-sm-7 text-start">${testName}</div>
          <div class="col-md-3 col-sm-5 text-center d-flex justify-content-between">
              <div style="width: 40px;">${count}</div>
              <div style="width: 100px;">${avgProc}%</div>
          </div> 
        </div>
      </div>
      `
    }).join('')}
  `
}
window.onload = getUsersData();

// TODO: add separate function to send GET requests

async function getUsersData() {
    const url = new URL(baseUrl + "/user/getUserTestsStatistic");
    const params = {};
    url.search = new URLSearchParams(params).toString();
    const response = await fetch(url);
    let result = await response.json();
    updateResult(result);
}

function reverseTests(target) {
    target.closest('#sortTestsButton').classList.toggle('reverse');
    const reversedTests = Array.from(resultTableBody.querySelectorAll('.test')).reverse();
    resultTableBody.innerHTML = '';
    reversedTests.forEach(test => {
        resultTableBody.append(test);
    })
}

dataContainer.addEventListener('click', (event) => {
    const {target} = event;
    if (target.closest('#sortTestsButton')) {
        isReverseTest = !isReverseTest;
        reverseTests(target);
    }
})