        // JavaScript code for filtering options based on search input (optional)
        const searchInput = document.querySelector('.search-input');
        const options = document.querySelectorAll('.multiselect-options label');

        searchInput.addEventListener('input', function () {
            const searchValue = this.value.toLowerCase();
            options.forEach(option => {
                const text = option.textContent.toLowerCase();
                if (text.includes(searchValue)) {
                    option.style.display = 'block';
                } else {
                    option.style.display = 'none';
                }
            });
        });
        
    document.addEventListener('DOMContentLoaded', function() {
    const checkboxes = document.querySelectorAll('.multiselect-options input[type="checkbox"]');
    const selectedChoices = document.getElementById('selectedChoices');
    const maxSelections = 2;

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            if (countSelectedCheckboxes() > maxSelections) {
                this.checked = false; // Uncheck the checkbox if the limit is exceeded
            }
            updateSelectedChoices();
        });
    });

    function countSelectedCheckboxes() {
        let count = 0;
        checkboxes.forEach(checkbox => {
            if (checkbox.checked) {
                count++;
            }
        });
        return count;
    }

    function updateSelectedChoices() {
        selectedChoices.innerHTML = ''; // Clear the existing selected choices
        let selectedCount = 0;

        checkboxes.forEach(checkbox => {
            if (checkbox.checked) {
                if (selectedCount < maxSelections) {
                    const label = checkbox.parentElement.textContent.trim();
                    const listItem = document.createElement('li');
                    listItem.textContent = label;
                    selectedChoices.appendChild(listItem);
                    selectedCount++;
                } else {
                    checkbox.checked = false; // Uncheck if the maximum limit is reached
                }
            }
        });
    }
});

       

