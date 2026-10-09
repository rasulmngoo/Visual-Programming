<?php
declare(strict_types=1);

/**
 * Функция безопасного экранирования вывода в HTML.
 */
function h(string $value): string
{
    return htmlspecialchars(
        $value,
        ENT_QUOTES | ENT_SUBSTITUTE,
        'UTF-8'
    );
}

/**
 * Безопасное извлечение строковых полей с проверкой типа.
 */
function postString(string $key): ?string
{
    $value = $_POST[$key] ?? null;
    return is_string($value) ? $value : null;
}

// Разрешенные списки значений (allow-list)
$allowedDistricts = [
    'Алмалинский',
    'Бостандыкский',
    'Медеуский',
    'Ауэзовский',
    'Жетысуский'
];

$allowedPaymentMethods = [
    'cash'   => 'Наличными при получении',
    'card'   => 'Картой курьеру',
    'online' => 'Онлайн-оплата'
];

$values = [
    'recipient'      => '',
    'phone'          => '',
    'district'       => '',
    'address'        => '',
    'payment_method' => '',
];

$errors = [];
$success = false;

// Обработка данных формы
if (($_SERVER['REQUEST_METHOD'] ?? 'GET') === 'POST') {
    $rawRecipient = postString('recipient');
    $rawPhone     = postString('phone');
    $rawDistrict  = postString('district');
    $rawAddress   = postString('address');
    $rawPayment   = postString('payment_method');

    // Нормализация данных
    $values['recipient']      = $rawRecipient === null ? '' : trim($rawRecipient);
    $values['phone']          = $rawPhone === null ? '' : trim($rawPhone);
    $values['district']       = $rawDistrict ?? '';
    $values['address']        = $rawAddress === null ? '' : trim($rawAddress);
    $values['payment_method'] = $rawPayment ?? '';

    // 1. Валидация: Получатель
    if ($rawRecipient === null || $values['recipient'] === '') {
        $errors['recipient'] = 'Укажите имя получателя.';
    } elseif (mb_strlen($values['recipient']) < 2 || mb_strlen($values['recipient']) > 100) {
        $errors['recipient'] = 'Имя получателя должно быть от 2 до 100 символов.';
    }

    // 2. Валидация: Телефон (шаблон)
    if ($rawPhone === null || $values['phone'] === '') {
        $errors['phone'] = 'Укажите номер телефона.';
    } elseif (!preg_match('/^(\+7|8)\d{10}$/', $values['phone'])) {
        $errors['phone'] = 'Введите телефон в формате +77001234567 или 87001234567.';
    }

    // 3. Валидация: Район (allow-list)
    if (!in_array($values['district'], $allowedDistricts, true)) {
        $errors['district'] = 'Выберите район из списка.';
    }

    // 4. Валидация: Адрес (10–200 символов)
    if ($rawAddress === null || $values['address'] === '') {
        $errors['address'] = 'Укажите адрес доставки.';
    } else {
        $length = mb_strlen($values['address']);
        if ($length < 10 || $length > 200) {
            $errors['address'] = 'Адрес должен содержать от 10 до 200 символов.';
        }
    }

    // 5. Валидация: Оплата (allow-list)
    if (!array_key_exists($values['payment_method'], $allowedPaymentMethods)) {
        $errors['payment_method'] = 'Выберите вариант оплаты.';
    }

    $success = ($errors === []);
}
?>
<!doctype html>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Заявка службы доставки — Вариант 10</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light py-5">
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white">
                    <h1 class="h4 mb-0">Заявка службы доставки</h1>
                </div>
                <div class="card-body">

                    <?php if ($success): ?>
                        <div class="alert alert-success" role="alert">
                            <h4 class="alert-heading">Заявка успешно принята!</h4>
                            <hr>
                            <p class="mb-1"><strong>Получатель:</strong> <?= h($values['recipient']) ?></p>
                            <p class="mb-1"><strong>Телефон:</strong> <?= h($values['phone']) ?></p>
                            <p class="mb-1"><strong>Район:</strong> <?= h($values['district']) ?></p>
                            <p class="mb-1"><strong>Адрес:</strong> <?= h($values['address']) ?></p>
                            <p class="mb-0"><strong>Оплата:</strong> <?= h($allowedPaymentMethods[$values['payment_method']]) ?></p>
                        </div>
                        <a href="" class="btn btn-outline-primary mt-2">Создать новую заявку</a>
                    <?php else: ?>

                        <form method="post" action="" novalidate>
                            <!-- Получатель -->
                            <div class="mb-3">
                                <label for="recipient" class="form-label fw-bold">Получатель (ФИО):</label>
                                <input type="text" 
                                       class="form-control <?= isset($errors['recipient']) ? 'is-invalid' : '' ?>" 
                                       id="recipient" 
                                       name="recipient" 
                                       value="<?= h($values['recipient']) ?>" required>
                                <?php if (isset($errors['recipient'])): ?>
                                    <div class="invalid-feedback d-block"><?= h($errors['recipient']) ?></div>
                                <?php endif; ?>
                            </div>

                            <!-- Телефон -->
                            <div class="mb-3">
                                <label for="phone" class="form-label fw-bold">Телефон (+7 / 8):</label>
                                <input type="tel" 
                                       class="form-control <?= isset($errors['phone']) ? 'is-invalid' : '' ?>" 
                                       id="phone" 
                                       name="phone" 
                                       placeholder="+77001234567" 
                                       value="<?= h($values['phone']) ?>" required>
                                <?php if (isset($errors['phone'])): ?>
                                    <div class="invalid-feedback d-block"><?= h($errors['phone']) ?></div>
                                <?php endif; ?>
                            </div>

                            <!-- Район -->
                            <div class="mb-3">
                                <label for="district" class="form-label fw-bold">Район доставки:</label>
                                <select class="form-select <?= isset($errors['district']) ? 'is-invalid' : '' ?>" 
                                        id="district" 
                                        name="district" required>
                                    <option value="">-- Выберите район --</option>
                                    <?php foreach ($allowedDistricts as $dist): ?>
                                        <option value="<?= h($dist) ?>" <?= $values['district'] === $dist ? 'selected' : '' ?>>
                                            <?= h($dist) ?>
                                        </option>
                                    <?php endforeach; ?>
                                </select>
                                <?php if (isset($errors['district'])): ?>
                                    <div class="invalid-feedback d-block"><?= h($errors['district']) ?></div>
                                <?php endif; ?>
                            </div>

                            <!-- Адрес -->
                            <div class="mb-3">
                                <label for="address" class="form-label fw-bold">Адрес (от 10 до 200 символов):</label>
                                <textarea class="form-control <?= isset($errors['address']) ? 'is-invalid' : '' ?>" 
                                          id="address" 
                                          name="address" 
                                          rows="3" required><?= h($values['address']) ?></textarea>
                                <?php if (isset($errors['address'])): ?>
                                    <div class="invalid-feedback d-block"><?= h($errors['address']) ?></div>
                                <?php endif; ?>
                            </div>

                            <!-- Способ оплаты -->
                            <div class="mb-3">
                                <label class="form-label fw-bold">Способ оплаты:</label>
                                <div>
                                    <?php foreach ($allowedPaymentMethods as $key => $label): ?>
                                        <div class="form-check">
                                            <input class="form-check-input <?= isset($errors['payment_method']) ? 'is-invalid' : '' ?>" 
                                                   type="radio" 
                                                   name="payment_method" 
                                                   id="payment_<?= h($key) ?>" 
                                                   value="<?= h($key) ?>" 
                                                   <?= $values['payment_method'] === $key ? 'checked' : '' ?>>
                                            <label class="form-check-label" for="payment_<?= h($key) ?>">
                                                <?= h($label) ?>
                                            </label>
                                        </div>
                                    <?php endforeach; ?>
                                </div>
                                <?php if (isset($errors['payment_method'])): ?>
                                    <div class="invalid-feedback d-block"><?= h($errors['payment_method']) ?></div>
                                <?php endif; ?>
                            </div>

                            <button type="submit" class="btn btn-primary w-100">Отправить заявку</button>
                        </form>

                    <?php endif; ?>

                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>